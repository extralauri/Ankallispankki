package com.example.extra.ankallispankki;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AccountListActivity extends MainActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Integer listId;
    ListView listView;
    Integer id=-1;  // variable for listview intent extra. becomes arraylist id in next activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Drawer navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //User id from login
        listId = Integer.parseInt(getIntent().getStringExtra("idc"));

        //Checks if the user is regular user and hides admin stuff from menu
        if(aBank.userList.get(listId).getID() == 0){
            navigationView.getMenu().setGroupVisible(R.id.nav_tools,false);
        }

        //Listiew
        listView = findViewById(R.id.listView2);
        listView.setClickable(true);

        //Arraylist for listAdapter
        final List<String> list = new ArrayList<String>();

        //Goes through users and accounts and adds them to arraylist
        for(int i=0;i<aBank.userList.size();i++){
            // variable for intent extra
            id++;
            for(int j=0;j<aBank.userList.get(i).accountList.size();j++){

            list.add("Account name: " + aBank.userList.get(i).getName() + " \nAccount number: " + aBank.userList.get(i).accountList.get(j).getAcNumber());
        }}

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);

        //creates listview and adds couple Intent extras so Admin can see other user's transfer history
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                Intent intent = new Intent(AccountListActivity.this, HistoryActivity.class);
                intent.putExtra("idc",listId.toString()); // sends intent extra (logged in user id) to next activity
                intent.putExtra("pos",id.toString());// sends intent extra (choosen user id) to next activity
                startActivity(intent);
                finish();


            }
        });
        listView.setAdapter(dataAdapter);

    }
    //Menudrawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        if (id == R.id.nav_user) {

            Intent intent = new Intent(this, UserActivity.class);
            intent.putExtra("idc",listId.toString());
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_account) {
            Intent intent = new Intent(this, AccountActivity.class);
            intent.putExtra("idc",listId.toString());
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_transfers) {

            Intent intent = new Intent(this, TransfersActivity.class);
            intent.putExtra("idc",listId.toString());
            startActivity(intent);
            finish();

        }else if (id == R.id.nav_payment) {

            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("idc",listId.toString());
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_history) {

            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra("idc",listId.toString());
            startActivity(intent);
            finish();

        }else if (id == R.id.nav_createAccount) {

            Intent intent = new Intent(this, CreateAccountActivity.class);
            intent.putExtra("idc",listId.toString());
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_createUser) {

            Intent intent = new Intent(this, CreateUserActivity.class);
            intent.putExtra("idc",listId.toString());
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_accountList) {

            Intent intent = new Intent(this, AccountListActivity.class);
            intent.putExtra("idc",listId.toString());
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_manageUsers) {

            Intent intent = new Intent(this, ManagerUserActivity.class);
            intent.putExtra("idc",listId.toString());
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
