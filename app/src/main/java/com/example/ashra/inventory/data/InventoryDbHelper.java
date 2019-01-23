package com.example.ashra.inventory.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.ashra.inventory.InventoryCursor;
import com.example.ashra.inventory.MainActivity;

/**
 * Created by ashra on 2/12/2018.
 */
public class InventoryDbHelper extends SQLiteOpenHelper {
    public final static String NAME_DB = "inventory.db";
    public final static int VERSION_DB= 1;

    public InventoryDbHelper(Context context) {
        super(context, NAME_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_STOCK = "CREATE TABLE " +
                InventoryContract.InventoryEntry.TABLE_NAME + "(" +
                InventoryContract.InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                InventoryContract.InventoryEntry.COLUMN_NAME + " TEXT NOT NULL," +
                InventoryContract.InventoryEntry.COLUMN_PRICE + " TEXT NOT NULL," +
                InventoryContract.InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0," +
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL," +
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE + " TEXT NOT NULL," +
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL," +
                InventoryContract.InventoryEntry.COLUMN_IMAGE + " TEXT NOT NULL" + ");";
        db.execSQL(CREATE_TABLE_STOCK);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void GreetItem(ListItems item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_NAME, item.getProductName());
        values.put(InventoryContract.InventoryEntry.COLUMN_PRICE, item.getPrice());
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, item.getQuantity());
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME, item.getSupplierName());
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE, item.getSupplierPhone());
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_EMAIL, item.getSupplierEmail());
        values.put(InventoryContract.InventoryEntry.COLUMN_IMAGE, item.getImage());
        long id = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
    }

    public Cursor readTable() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRICE,
                InventoryContract.InventoryEntry.COLUMN_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_EMAIL,
                InventoryContract.InventoryEntry.COLUMN_IMAGE

        };
        Cursor cursor = db.query(
                InventoryContract.InventoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;

    }

    public Cursor ReadItem(long itemId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRICE,
                InventoryContract.InventoryEntry.COLUMN_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_EMAIL,
                InventoryContract.InventoryEntry.COLUMN_IMAGE
        };
        String selection = InventoryContract.InventoryEntry._ID + "=?";
        String[] selectionArgs = new String[] { String.valueOf(itemId) };

        Cursor cursor = db.query(
                InventoryContract.InventoryEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return cursor;
    }

    public void editItem(long currentItemId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, quantity);
        String selection = InventoryContract.InventoryEntry._ID + "=?";
        String[] selectionArgs = new String[] { String.valueOf(currentItemId) };
        db.update(InventoryContract.InventoryEntry.TABLE_NAME,
                values, selection, selectionArgs);
    }

    public void sellItem(long itemId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        int newQuantity = 0;
        if (quantity > 0) {
            newQuantity = quantity -1;
        }
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, newQuantity);
        String selection = InventoryContract.InventoryEntry._ID + "=?";
        String[] selectionArgs = new String[] { String.valueOf(itemId) };
        db.update(InventoryContract.InventoryEntry.TABLE_NAME,
                values, selection, selectionArgs);
    }
}
