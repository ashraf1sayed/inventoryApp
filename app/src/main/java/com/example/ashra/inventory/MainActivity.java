package com.example.ashra.inventory;


import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.ashra.inventory.data.InventoryDbHelper;
import com.example.ashra.inventory.data.ListItems;

public class MainActivity extends AppCompatActivity {
    InventoryDbHelper inventoryDbHelper;
    InventoryCursor myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inventoryDbHelper = new InventoryDbHelper(this);


        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        final ListView listView = (ListView) findViewById(R.id.list_view);
        View emptyText = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyText);
        Cursor cursor = inventoryDbHelper.readTable();
        myAdapter = new InventoryCursor(this, cursor);
        listView.setAdapter(myAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        myAdapter.swapCursor(inventoryDbHelper.readTable());
    }

    public void clickItem(long id) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra("itemId", id);
        startActivity(intent);
    }

    public void Sale(long id, int quantity) {
        inventoryDbHelper.sellItem(id, quantity);
        myAdapter.swapCursor(inventoryDbHelper.readTable());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_dummy_data:
                addData();
                myAdapter.swapCursor(inventoryDbHelper.readTable());
        }
        return super.onOptionsItemSelected(item);
    }


    private void addData() {
        ListItems DESFERAL = new ListItems(
                "DESFERAL",
                "112 €",
                75,
                "Ashraf Sayed",
                "01127235020",
                "as4268300@gmail.com",
                "android.resource://com.example.ashra.inventory/drawable/images");
        inventoryDbHelper.GreetItem(DESFERAL);

        ListItems FEROMIN = new ListItems(
                "FEROMIN",
                "100 €",
                54,
                "Ashraf Sayed",
                "01127235020",
                "as4268300@gmail.com",
                "android.resource://com.example.ashra.inventory/drawable/image");
        inventoryDbHelper.GreetItem(FEROMIN);

        ListItems ALDACTONE = new ListItems(
                "ALDACTONE",
                "42 €",
                94,
                "Ashraf Sayed",
                "01120307589",
                "as4268300@gmail.com",
                "android.resource://com.example.ashra.inventory/drawable/as");
        inventoryDbHelper.GreetItem(ALDACTONE);

        ListItems DEXAMED = new ListItems(
                "DEXAMED",
                "17 €",
                44,
                "Ashraf Sayed",
                "01123705060",
                "as4268300@gmail.com",
                "android.resource://com.example.ashra.inventory/drawable/asf");
        inventoryDbHelper.GreetItem(DEXAMED);

        ListItems NORLEVO = new ListItems(
                "NORLEVO",
                "20 €",
                34,
                "Ashraf Sayed",
                "01127235020",
                "as4268300@gmail.com",
                "android.resource://com.example.ashra.inventory/drawable/ash");
        inventoryDbHelper.GreetItem(NORLEVO);

        ListItems DECADRON = new ListItems(
                "DECADRON",
                "18 €",
                276,
                "Ashraf Sayed",
                "01127232050",
                "as4268300@gmail.com",
                "android.resource://com.example.ashra.inventory/drawable/ashf");
        inventoryDbHelper.GreetItem(DECADRON);


    }
}
