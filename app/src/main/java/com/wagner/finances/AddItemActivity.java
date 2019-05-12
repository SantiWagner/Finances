package com.wagner.finances;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddItemActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        if(getIntent().getExtras().getBoolean("editing")){
            EditText name=(EditText)findViewById(R.id.itemName);
            EditText amount=(EditText)findViewById(R.id.itemAmount);
            TextView currency=(TextView)findViewById(R.id.itemCurrency);
            Item item = (Item)getIntent().getExtras().get("item");

            name.setText(item.name);
            amount.setText(item.amount+"");
            currency.setText("Currency: "+item.currency);

            Button deleteButton = findViewById(R.id.deleteButton);
            deleteButton.setVisibility(View.VISIBLE);
        }
    }

    public void addNewItem(View v){
        EditText name=(EditText)findViewById(R.id.itemName);
        EditText amount=(EditText)findViewById(R.id.itemAmount);
        TextView currency=(TextView)findViewById(R.id.itemCurrency);
        if(getIntent().getExtras().getBoolean("editing")) {


            Item item = (Item)getIntent().getExtras().get("item");
            item.name = name.getText().toString();
            item.amount = Double.parseDouble(amount.getText().toString());
            item.last_updated = System.currentTimeMillis();
            item.currency = currency.getText().toString().split(":")[1].substring(1);

            MainActivity.db.itemDao().updateItem(item);
        }else {
            MainActivity.db.itemDao().insertItem(new Item(name.getText().toString(), Double.parseDouble(amount.getText().toString()), System.currentTimeMillis(),currency.getText().toString().split(":")[1].substring(1)));
        }

        goBack(null);
    }

    public void goBack(View v){
        finish();
    }

    public void deleteItem(View v){
        Item item = (Item)getIntent().getExtras().get("item");
        MainActivity.db.itemDao().deleteItem(item);
        goBack(null);
    }

    public void openCurrenciesDialog(View v){
        final String[] currencies = new String[]{
                "USD",
                "ARS",

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Currency:");
        builder.setItems(currencies, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                ((TextView)findViewById(R.id.itemCurrency)).setText("Currency: "+currencies[item]);
            }
        }).show();
    }

}
