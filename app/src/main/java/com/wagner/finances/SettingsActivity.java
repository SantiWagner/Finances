package com.wagner.finances;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements  Constants {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
       preferences = getSharedPreferences(INDICATORS_FILE, 0);
        float cost_of_living = preferences.getFloat("COST_OF_LIVING",-1);
        float goal = preferences.getFloat("GOAL",-1);

        if(cost_of_living != -1) {
            EditText col_text = (EditText) findViewById(R.id.colValue);
            col_text.setText(""+cost_of_living);
        }

        if(goal != -1) {
            EditText goal_text = (EditText) findViewById(R.id.goalValue);
            goal_text.setText(""+goal);
        }

    }


    public void goBack(View v){
        SharedPreferences.Editor editor = preferences.edit();
        EditText col_text = (EditText) findViewById(R.id.colValue);
        EditText goal_text = (EditText) findViewById(R.id.goalValue);

        if(!col_text.getText().toString().equals("Cost of Living value"))
            editor.putFloat("COST_OF_LIVING", Float.parseFloat(col_text.getText().toString()));
        if(!goal_text.getText().toString().equals("Goal value"))
            editor.putFloat("GOAL", Float.parseFloat(goal_text.getText().toString()));

        editor.commit();
        finish();
    }


    public void openCurrenciesDialog(View v){
        final String[] currencies = new String[]{
                "USD"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Currency:");
        builder.setItems(currencies, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                ((TextView)findViewById(R.id.itemCurrency)).setText("Currency: "+currencies[item]);
            }
        }).show();
    }

    @Override
    public  void onBackPressed(){
        goBack(null);
    }

}
