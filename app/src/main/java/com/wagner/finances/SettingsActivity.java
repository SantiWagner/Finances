package com.wagner.finances;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements  Constants {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preferences = getSharedPreferences(INDICATORS_FILE, 0);
        float cost_of_living_usd = preferences.getFloat("COST_OF_LIVING_USD",-1);
        float goal_usd = preferences.getFloat("GOAL_USD",-1);

        float cost_of_living_ars = preferences.getFloat("COST_OF_LIVING_ARS",-1);
        float goal_ars = preferences.getFloat("GOAL_ARS",-1);

        if(cost_of_living_usd != -1) {
            EditText col_text = (EditText) findViewById(R.id.colValueUSD);
            col_text.setText(""+cost_of_living_usd);
        }

        if(goal_usd != -1) {
            EditText goal_text = (EditText) findViewById(R.id.goalValueUSD);
            goal_text.setText(""+goal_usd);
        }


        if(cost_of_living_ars != -1) {
            EditText col_text_ars = (EditText) findViewById(R.id.colValueARS);
            col_text_ars.setText(""+cost_of_living_ars);
        }

        if(goal_ars != -1) {
            EditText goal_text_ars = (EditText) findViewById(R.id.goalValueARS);
            goal_text_ars.setText(""+goal_ars);
        }


    }


    public void goBack(View v){
        SharedPreferences.Editor editor = preferences.edit();

        EditText col_text_usd = (EditText) findViewById(R.id.colValueUSD);
        EditText goal_text_usd = (EditText) findViewById(R.id.goalValueUSD);
        EditText col_text_ars = (EditText) findViewById(R.id.colValueARS);
        EditText goal_text_ars = (EditText) findViewById(R.id.goalValueARS);

        if(!col_text_usd.getText().toString().equals(""))
            editor.putFloat("COST_OF_LIVING_USD", Float.parseFloat(col_text_usd.getText().toString()));
        if(!goal_text_usd.getText().toString().equals(""))
            editor.putFloat("GOAL_USD", Float.parseFloat(goal_text_usd.getText().toString()));

        if(!col_text_ars.getText().toString().equals(""))
            editor.putFloat("COST_OF_LIVING_ARS", Float.parseFloat(col_text_ars.getText().toString()));
        if(!goal_text_ars.getText().toString().equals(""))
            editor.putFloat("GOAL_ARS", Float.parseFloat(goal_text_ars.getText().toString()));

        editor.commit();
        finish();
    }

    @Override
    public  void onBackPressed(){
        goBack(null);
    }

}
