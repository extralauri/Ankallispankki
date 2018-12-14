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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateUserActivity extends MainActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //UI variables
    TextView Username, password, Address, Age, City;
    Button save, create, delete;
    Spinner spinner;

    //variables for arrays and spinner
    Integer i=0, spinnerSelection,listId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Intent extra for user management
        listId = Integer.parseInt(getIntent().getStringExtra("idc"));
        // hide menu options from regular users
        if(aBank.userList.get(listId).getID() == 0){
            navigationView.getMenu().setGroupVisible(R.id.nav_tools,false);
        }
        //UI elements
        Username = findViewById(R.id.editTextUser);
        password = findViewById(R.id.editTextUserpassw);
        Address = findViewById(R.id.editTextAddressw);
        Age = findViewById(R.id.editText6);
        City = findViewById(R.id.editText7);
        create = findViewById(R.id.button3);
        save = findViewById(R.id.button13);
        delete = findViewById(R.id.button7);

        addSpinner();
    }

    //Adds new user with given data
    public void SaveUser(View V){
        aBank.userList.add(new User(Username.getText().toString(),password.getText().toString(),Address.getText().toString(),City.getText().toString(),Integer.parseInt(Age.getText().toString()),0));
        addSpinner(); //updates spinner
    }

    //Spinner
    public void addSpinner(){
        spinner = findViewById(R.id.spinner4);
        final List<String> list = new ArrayList<String>();

        //Populates list with usernames
        for(int i=0;i<aBank.userList.size();i++){
            list.add(aBank.userList.get(i).getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //Removes account
    public void setDeleteAccount(View v) {
        if (spinnerSelection == 0) {
            Toast.makeText(this, "Admin can't be removed!", Toast.LENGTH_LONG).show();
        } else {
            aBank.userList.remove((int) spinnerSelection);
            addSpinner();
        }
    }

    //Menu drawer
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
