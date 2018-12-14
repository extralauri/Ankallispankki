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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TransfersActivity extends MainActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //variables for UI elements
    Button button;
    TextView debitFrom, debitTo;
    Spinner spinnerFrom, spinnerTo;
    SeekBar seekbar;
    //Helper variables for arrays
    Integer i=0, j=0, transMoney=0, listId;
    // variables for transferMoney method call
    String trans_from, trans_to, trans_name, trans_message, trans_money, trans_filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfers);
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

        // User id from login
        listId = Integer.parseInt(getIntent().getStringExtra("idc"));

        // Hide admin tools from regular users
        if(aBank.userList.get(listId).getID() == 0){
            navigationView.getMenu().setGroupVisible(R.id.nav_tools,false);
        }

        //static values for logged in user
        trans_name = aBank.userList.get(listId).getName();
        trans_filename = aBank.userList.get(listId).getName()+"_history.json";


        //UI elements
        debitFrom = findViewById(R.id.textView32);
        debitTo = findViewById(R.id.textView33);
        debitFrom.setText(String.valueOf(aBank.userList.get(listId).accountList.get(i).getDebit()));
        debitTo.setText(String.valueOf(aBank.userList.get(listId).accountList.get(i).getDebit().toString()));

        seekbar = findViewById(R.id.seekBar5);
        seekbar.setMax(1000);

        //Seekbar Listener
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getApplicationContext(),"More money:  "+progress, Toast.LENGTH_SHORT).show();
                transMoney = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(),"More money: 0", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(getApplicationContext(),"Lisää rahaa:  "+progress, Toast.LENGTH_SHORT).show();
            }
        });

        //Spinner
        addSpinner();
    }
    //spinner builder
    public void addSpinner(){
        //Spinner UI elements
        spinnerFrom = findViewById(R.id.spinner5);
        spinnerTo = findViewById(R.id.spinner6);

        final List<String> list = new ArrayList<String>();

        //populate spinner with arraylist
        for(int i=0;i<aBank.userList.get(listId).accountList.size();i++){
            list.add(aBank.userList.get(listId).accountList.get(i).getAcNumber());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(dataAdapter);
        spinnerTo.setAdapter(dataAdapter);
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i = position;
                debitFrom.setText(String.valueOf(aBank.userList.get(listId).accountList.get(i).getDebit()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                j = position;
                debitTo.setText(String.valueOf(aBank.userList.get(listId).accountList.get(j).getDebit().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Method to transfer money from one account to other
    public void transferMoney(View v) throws JSONException {

        //checks if the account is disabled
        if (aBank.userList.get(listId).accountList.get(i).disabled == 1) {
            Toast.makeText(this, "The account has been disabled", Toast.LENGTH_SHORT).show();
        }
        //checks if enough money has been selected
        else if (transMoney == 0){
            Toast.makeText(this,"Select more money",Toast.LENGTH_SHORT).show();
        }
        //checks if spinner selections are the same
        else if(i.equals(j)){
            Toast.makeText(this,"Select two different accounts",Toast.LENGTH_SHORT).show();
        }
        else {
            //checks if there's enough money on the account
            if (aBank.userList.get(listId).accountList.get(i).getDebit() < transMoney) {
                Toast.makeText(this, "Not enough money!", Toast.LENGTH_LONG).show();
            } else {
                //removes money from selected account
                aBank.userList.get(listId).accountList.get(i).withDraw(transMoney);
                //adds money to selected account
                aBank.userList.get(listId).accountList.get(j).deposit(transMoney);
                Toast.makeText(this, "Transfer completed", Toast.LENGTH_LONG).show();
                //updates money and credit textfields
                debitFrom.setText(String.valueOf(aBank.userList.get(listId).accountList.get(i).getDebit()));
                debitTo.setText(String.valueOf(aBank.userList.get(listId).accountList.get(j).getDebit().toString()));
                // sets user data values to variables
                trans_from = aBank.userList.get(listId).accountList.get(i).getAcNumber();
                trans_to = aBank.userList.get(listId).accountList.get(j).getAcNumber();
                trans_message = "Transferred from another account";
                trans_money = String.valueOf(transMoney);
                //calls write to file method
                aBank.userList.get(listId).accountList.get(i).transaction.writeJSONFile(trans_filename, trans_name, trans_from, trans_to, trans_money, trans_message);
            }
        }
    }

        //DrawerMenu
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
