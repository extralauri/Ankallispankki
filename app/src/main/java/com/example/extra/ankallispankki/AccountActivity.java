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

public class AccountActivity extends MainActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //variables for UI elements
    TextView accountName, accountDebit, accountCredit;
    Spinner spinner;
    Button addMoney, lessMoney, lessCreditMoney;
    SeekBar seekbar;
    //Helper variables for arrays
    Integer money=0, i=0, listId;
    // variables for transferMoney method call'
    String trans_from, trans_to, trans_name, trans_message, trans_money, trans_filename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
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
        //UI elements
        accountName = findViewById(R.id.textView6);
        accountDebit = findViewById(R.id.textView12);
        accountCredit = findViewById(R.id.textView13);
        // User id from login
        listId = Integer.parseInt(getIntent().getStringExtra("idc"));

        // Hide admin tools from regular users
        if(aBank.userList.get(listId).getID() == 0){
            navigationView.getMenu().setGroupVisible(R.id.nav_tools,false);
        }
        //static values for logged in user
        trans_name = aBank.userList.get(listId).getName();
        trans_filename = aBank.userList.get(listId).getName()+"_history.json";


        accountName.setText(aBank.userList.get(listId).getName());
        accountDebit.setText(String.valueOf(aBank.userList.get(listId).accountList.get(i).getDebit()));
        accountCredit.setText(String.valueOf(aBank.userList.get(listId).accountList.get(i).getCredit()));

        //UI elements
        addSpinner();
        addMoney = findViewById(R.id.button6);
        lessMoney = findViewById(R.id.button7);
        lessCreditMoney = findViewById(R.id.button10);

        //SeekBar
        seekbar = findViewById(R.id.seekBar4);
        seekbar.setMax(1000);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getApplicationContext(),"Money:  "+progress, Toast.LENGTH_SHORT).show();
                if(money == 0){
                    Toast.makeText(getApplicationContext(),"Add more money:  "+progress, Toast.LENGTH_SHORT).show();
                }
                    money = progress;

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

    }

    //Spinner
    public void addSpinner(){
        spinner = findViewById(R.id.spinner);
        final List<String> list = new ArrayList<String>();

        for(int i=0;i<aBank.userList.get(listId).accountList.size();i++){
            list.add(aBank.userList.get(listId).accountList.get(i).acNumber);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i = position;
                accountDebit.setText(String.valueOf(aBank.userList.get(listId).accountList.get(i).getDebit()));
                accountCredit.setText(String.valueOf(aBank.userList.get(listId).accountList.get(i).getCredit()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Method to add money to account when button is clicked
    public void moreMoney(View v) throws JSONException {
        //User account number
        trans_from = aBank.userList.get(listId).accountList.get(i).acNumber;
        //checks whether account is disabled or not
        if(aBank.userList.get(listId).accountList.get(i).disabled == 1){
            Toast.makeText(this,"The account has been disabled",Toast.LENGTH_SHORT).show();
        }
        //checks if there's money selected on the seekbar
        else if (money == 0){
            Toast.makeText(this,"Select more money",Toast.LENGTH_SHORT).show();
        }
        //adds money to account and writes transaction to file
        else {
            trans_message = "Deposit money";
            trans_to = trans_from;
            trans_money = String.valueOf(money);
            aBank.userList.get(listId).accountList.get(i).deposit(money);
            aBank.userList.get(listId).accountList.get(i).transaction.writeJSONFile(trans_filename, trans_name, trans_from, trans_to, trans_money, trans_message);
            updateText();
        }
    }

    public void lessMoney(View v) throws JSONException {
        //User account number
        trans_from = aBank.userList.get(listId).accountList.get(i).acNumber;
        //checks whether account is disabled or not
        if(aBank.userList.get(listId).accountList.get(i).disabled == 1){
            Toast.makeText(this,"The account has been disabled",Toast.LENGTH_SHORT).show();
        }
        //checks if there's money selected on the seekbar
        else if (money == 0){
            Toast.makeText(this,"Select more money",Toast.LENGTH_SHORT).show();
        }
        //checks if there's enough money on the account
        else if(aBank.userList.get(listId).accountList.get(i).getDebit() < money){

            Toast.makeText(this,"Not enough debit",Toast.LENGTH_SHORT).show();
        }
        //removes moneyu from account and writes transactions to file
        else {
            trans_message = "Withdraw money";
            trans_to = trans_from;
            trans_money = String.valueOf(money);
            aBank.userList.get(listId).accountList.get(i).withDraw(money);
            aBank.userList.get(listId).accountList.get(i).transaction.writeJSONFile(trans_filename, trans_name, trans_from, trans_to, trans_money, trans_message);
            updateText();
        }
    }
    public void lessCreditMoney(View v) throws JSONException {
        //User account number
        trans_from = aBank.userList.get(listId).accountList.get(i).acNumber;
        //checks whether account is disabled or not
        if(aBank.userList.get(listId).accountList.get(i).disabled == 1){
            Toast.makeText(this,"The account has been disabled",Toast.LENGTH_SHORT).show();
        }
        //checks if there's money selected on the seekbar
        else if (money == 0){
            Toast.makeText(this,"Select more money",Toast.LENGTH_SHORT).show();
        }
        //checks if there's enough credit on the account
        else if(aBank.userList.get(listId).accountList.get(i).getCredit() < money){

            Toast.makeText(this,"Not enough credit",Toast.LENGTH_SHORT).show();
        }
        else {
            // removes money from credit account and writes transaction to file
            trans_message = "Withdraw using credit money";
            trans_to = trans_from;
            trans_money = String.valueOf(money);
            aBank.userList.get(listId).accountList.get(i).withDrawCredit(money);
            aBank.userList.get(listId).accountList.get(i).transaction.writeJSONFile(trans_filename, trans_name, trans_from, trans_to, trans_money, trans_message);
            updateText();
        }
    }
    // updates account debit and credit balance to textviews
    public void updateText(){
        accountDebit.setText(String.valueOf(aBank.userList.get(listId).accountList.get(i).getDebit()));
        accountCredit.setText(String.valueOf(aBank.userList.get(listId).accountList.get(i).getCredit()));
    }

    //Navigation menu
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
