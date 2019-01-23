package com.example.ashra.inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ashra.inventory.data.InventoryContract;
import com.example.ashra.inventory.data.InventoryDbHelper;
import com.example.ashra.inventory.data.ListItems;

/**
 * Created by ashra on 2/12/2018.
 */
public class EditorActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private InventoryDbHelper DbHelper;
    EditText name;
    EditText price;
    EditText quantity;
    EditText supplierName;
    EditText supplierPhone;
    EditText supplierEmail;
    long Id;
    Button decreaseQuantity;
    Button increaseQuantity;
    Button imageBtn;
    ImageView imageView;
    Uri Uri;
    private static final int PICK_IMAGE_REQUEST = 0;
    Boolean infoItemHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        name = (EditText) findViewById(R.id.product_name_edit);
        price = (EditText) findViewById(R.id.price_edit);
        quantity = (EditText) findViewById(R.id.quantity_edit);
        supplierName = (EditText) findViewById(R.id.supplier_name_edit);
        supplierPhone = (EditText) findViewById(R.id.supplier_phone_edit);
        supplierEmail = (EditText) findViewById(R.id.supplier_email_edit);
        decreaseQuantity = (Button) findViewById(R.id.decrease_quantity);
        increaseQuantity = (Button) findViewById(R.id.increase_quantity);
        imageBtn = (Button) findViewById(R.id.select_image);
        imageView = (ImageView) findViewById(R.id.image_view);

        DbHelper = new InventoryDbHelper(this);
        Id = getIntent().getLongExtra("itemId", 0);
        if (Id == 0) {
            setTitle("Add new item");
        } else {
            setTitle("Edit item");
            addValues(Id);
        }

        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subtractOneToQuantity();
                infoItemHasChanged = true;
            }
        });

        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sumOneToQuantity();
                infoItemHasChanged = true;
            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImageSelector();
                infoItemHasChanged = true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!infoItemHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedDialog(discardButtonClickListener);
    }

    private void showUnsavedDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("There are unsaved changes in the product information. Do you want to continue?");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void subtractOneToQuantity() {
        String previousValueString = quantity.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            return;
        } else if (previousValueString.equals("0")) {
            return;
        } else {
            previousValue = Integer.parseInt(previousValueString);
            quantity.setText(String.valueOf(previousValue - 1));
        }
    }

    private void sumOneToQuantity() {
        String previousValueString = quantity.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            previousValue = 0;
        } else {
            previousValue = Integer.parseInt(previousValueString);
        }
        quantity.setText(String.valueOf(previousValue + 1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (Id == 0) {
            MenuItem deleteOneItemMenuItem = menu.findItem(R.id.action_delete_item);
            MenuItem deleteAllMenuItem = menu.findItem(R.id.action_delete_all_data);
            MenuItem orderMenuItem = menu.findItem(R.id.action_order);
            deleteOneItemMenuItem.setVisible(false);
            deleteAllMenuItem.setVisible(false);
            orderMenuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                if (!addItem()) {

                    return true;
                }
                finish();
                return true;
            case android.R.id.home:
                if (!infoItemHasChanged) {
                    Intent intent = new Intent(EditorActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //NavUtils.navigateUpFromSameTask(EditorActivity.this);
                                Intent intent = new Intent(EditorActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        };
                showUnsavedDialog(discardButtonClickListener);
                return true;
            case R.id.action_order:

                showOrderConfirmationDialog();
                return true;
            case R.id.action_delete_item:

                showDeleteDialog(Id);
                return true;
            case R.id.action_delete_all_data:

                showDeleteDialog(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean addItem() {
        boolean isAllOk = true;
        if (!checkValueSet(name, "name")) {
            isAllOk = false;
        }
        if (!checkValueSet(price, "price")) {
            isAllOk = false;
        }
        if (!checkValueSet(quantity, "quantity")) {
            isAllOk = false;
        }
        if (!checkValueSet(supplierName, "supplier name")) {
            isAllOk = false;
        }
        if (!checkValueSet(supplierPhone, "supplier phone")) {
            isAllOk = false;
        }
        if (!checkValueSet(supplierEmail, "supplier email")) {
            isAllOk = false;
        }
        if (Uri == null && Id == 0) {
            isAllOk = false;
            imageBtn.setError("Missing image");
        }
        if (!isAllOk) {
            return false;
        }

        if (Id == 0) {
            ListItems item = new ListItems(
                    name.getText().toString().trim(),
                    price.getText().toString().trim(),
                    Integer.parseInt(quantity.getText().toString().trim()),
                    supplierName.getText().toString().trim(),
                    supplierPhone.getText().toString().trim(),
                    supplierEmail.getText().toString().trim(),
                    Uri.toString());
            DbHelper.GreetItem(item);

        } else {
            int quantityS = Integer.parseInt(quantity.getText().toString().trim());
            DbHelper.editItem(Id, quantityS);
        }
        return true;
    }

    private boolean checkValueSet(EditText text, String description) {
        if (TextUtils.isEmpty(text.getText())) {
            text.setError("Missing product " + description);
            return false;
        } else {
            text.setError(null);
            return true;
        }
    }

    private void addValues(long itemId) {
        Cursor cursor = DbHelper.ReadItem(itemId);
        cursor.moveToFirst();
        name.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_NAME)));
        price.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE)));
        quantity.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY)));
        supplierName.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME)));
        supplierPhone.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE)));
        supplierEmail.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_EMAIL)));
        imageView.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_IMAGE))));
        name.setEnabled(false);
        price.setEnabled(false);
        supplierName.setEnabled(false);
        supplierPhone.setEnabled(false);
        supplierEmail.setEnabled(false);
        imageBtn.setEnabled(false);
    }

    private void showOrderConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You can place an order for this item by phone or e-mail");
        builder.setPositiveButton("Phone", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + supplierPhone.getText().toString().trim()));
                startActivity(intent);
            }
        });
        builder.setNegativeButton("E-mail", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(android.content.Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:" + supplierEmail.getText().toString().trim()));
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Recurrent new order");
                String bodyMessage = "Please send us as soon as possible more " +
                        name.getText().toString().trim() +
                        "!!!";
                intent.putExtra(android.content.Intent.EXTRA_TEXT, bodyMessage);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private int deleteAllRowsFromTable() {
        SQLiteDatabase database = DbHelper.getWritableDatabase();
        return database.delete(InventoryContract.InventoryEntry.TABLE_NAME, null, null);
    }

    private int deleteOneItemFromTable(long itemId) {
        SQLiteDatabase database = DbHelper.getWritableDatabase();
        String selection = InventoryContract.InventoryEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(itemId)};
        int rowsDeleted = database.delete(
                InventoryContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
        return rowsDeleted;
    }

    private void showDeleteDialog(final long itemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to procede with the deletion?");
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (itemId == 0) {
                    deleteAllRowsFromTable();
                } else {
                    deleteOneItemFromTable(itemId);
                }
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void OpenImageSelector() {
        openSelectImage();
    }

    private void openSelectImage() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openSelectImage();

                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {

            if (resultData != null) {
                Uri = resultData.getData();
                imageView.setImageURI(Uri);
                imageView.invalidate();
            }
        }
    }
}
