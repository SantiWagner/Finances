package com.wagner.finances;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements DashboardFragment.OnFragmentInteractionListener, ListFragment.OnFragmentInteractionListener {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:{
                    Fragment fragment = new InformationFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.mainActivityFragment, fragment);
                    transaction.commit();
                    return true;
                }
                case R.id.navigation_dashboard: {
                    Fragment fragment = new DashboardFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.mainActivityFragment, fragment);
                    transaction.commit();
                    return true;
                }
                case R.id.navigation_notifications: {
                    Fragment fragment = new ListFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.mainActivityFragment, fragment);
                    transaction.commit();
                    return true;
                }
            }
            return false;
        }
    };
    public static FinancesDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Room.databaseBuilder(this,
                FinancesDatabase.class, "database-finances").allowMainThreadQueries().build();
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);

        Fragment fragment = new DashboardFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.mainActivityFragment, fragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void addItemCallback(View v){
        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtra("editing",false);
        startActivity(intent);
    }

    public void editItemCallback(View v){
        Item item = db.itemDao().getItem(v.getId());
        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtra("editing",true);
        intent.putExtra("item",item);
        startActivity(intent);
    }


}
