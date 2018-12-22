package com.example.extra.ankallispankki;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HistoryActivity extends MainActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Integer listId, tmpId;
    Context context = null;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> from = new ArrayList<>();
    ArrayList<String> to = new ArrayList<>();
    ArrayList<String> money = new ArrayList<>();
    ArrayList<String> message = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        context = HistoryActivity.this;

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Variable for Intent extra from drawer
       listId = Integer.parseInt(getIntent().getStringExtra("idc"));
        // Variable for Intent extra from AccountListActivity
        tmpId = Integer.parseInt(getIntent().getStringExtra("idc"));


        if (getIntent().hasExtra("pos")){
            listId = Integer.parseInt(getIntent().getStringExtra("pos"));
            System.out.println("listId on pos: "+ listId );
        }
        //creates transaction history file
        String filename = aBank.userList.get(listId).getName()+"_history.json";
        File file = new File(context.getFilesDir(), filename);
        try {
            if (file.createNewFile()) {

                System.out.println("File has been created.");
            } else {

                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get the reference of RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        try {
            // JSONObject from JSON file
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // JSONArray named users
            JSONArray userArray = obj.getJSONArray("users");
            // implement for loop for getting users list data
            for (int i = 0; i < userArray.length(); i++) {
                // JSONObject for user data
                JSONObject userDetail = userArray.getJSONObject(i);
                names.add(userDetail.getString("name"));
                from.add("From: " + userDetail.getString("from"));
                to.add("To: " + userDetail.getString("to"));
                money.add("Money: " + userDetail.getString("money"));
                message.add("Message: " + userDetail.getString("message"));
                time.add("Time: " + userDetail.getString("time"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  call the constructor of HistoryAdapter to send the reference and data to Adapter
        HistoryAdapter historyAdapter = new HistoryAdapter(this, names, from, to, money, message,time);
        recyclerView.setAdapter(historyAdapter); // set the Adapter to RecyclerView

        listId = tmpId;  //Intent extra back to listId

        //Drawer Navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Hide menu option from normal users
        if(aBank.userList.get(listId).getID() == 0){
            navigationView.getMenu().setGroupVisible(R.id.nav_tools,false);
        }
    }

    // Method for reading and parsing data from a file.
    public String loadJSONFromAsset() {
       String json = null;
        try {
            InputStream is =  openFileInput(aBank.userList.get(listId).getName()+"_history.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    // Drawer selection
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
