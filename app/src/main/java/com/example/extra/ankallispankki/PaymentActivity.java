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
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends MainActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //UI variables
    Spinner spinner;
    Button button, button2, button3;
    SeekBar seekbar;
    EditText editText, editText2;
    TextView accountDebit, accountCredit;

    // variables for payOnClick method call'
    String trans_from, trans_to, trans_name, trans_message="", trans_money="0", trans_filename;

    //Helper variables for arrays
    Integer i=0,h=-1,k=-1,listId, money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Menu drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listId = Integer.parseInt(getIntent().getStringExtra("idc"));

        if(aBank.userList.get(listId).getID() == 0){
            navigationView.getMenu().setGroupVisible(R.id.nav_tools,false);
        }

        //Sets static user variables for methods
        trans_name = aBank.userList.get(listId).getName();
        trans_filename = aBank.userList.get(listId).getName()+"_history.json";

        //Seekbar
        seekbar = findViewById(R.id.seekBar3);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getApplicationContext(),"Lisää rahaa:  "+progress, Toast.LENGTH_SHORT).show();
                money = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(),"Lisää rahaa: 0", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(getApplicationContext(),"Lisää rahaa:  "+progress, Toast.LENGTH_SHORT).show();
            }
        });

        //UI elements
        addSpinner();
        editText2 = findViewById(R.id.messageText);
        button = findViewById(R.id.button5);
        button2 = findViewById(R.id.button11);
        button3 = findViewById(R.id.button13);
        editText = findViewById(R.id.editText8);
        accountDebit = findViewById(R.id.textView26);
        accountCredit = findViewById(R.id.textView28);
        accountDebit.setText(String.valueOf(aBank.userList.get(listId).accountList.get(i).getDebit()));
        accountCredit.setText(String.valueOf(aBank.userList.get(listId).accountList.get(i).getCredit()));

    }
    //Spinner
    public void addSpinner(){
        spinner = findViewById(R.id.spinner2);
        final List<String> list = new ArrayList<String>();

        //populates list with account numbers
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
                //sets max value for seekbar
                seekbar.setMax(aBank.userList.get(listId).accountList.get(i).getDebit().intValue()+aBank.userList.get(listId).accountList.get(i).getCredit().intValue());
                updateText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Updates textviews
    public void updateText(){
        accountDebit.setText(String.valueOf(aBank.userList.get(listId).accountList.get(i).getDebit()));
        accountCredit.setText(String.valueOf(aBank.userList.get(listId).accountList.get(i).getCredit()));
    }

    // makes payment when Pay button is clicked
    public void payOnClick(View v) throws JSONException {

        //temp variable
        String temp;
        //Checks if account is disabled
        if(aBank.userList.get(listId).accountList.get(i).disabled == 1){
            Toast.makeText(this,"The account has been disabled",Toast.LENGTH_SHORT).show();
        }
        //checks if there's money selected on seekbar
        else if (money == 0){
            Toast.makeText(this,"Select more money",Toast.LENGTH_SHORT).show();
        }
        else {
            //sets values to variables which is used writing on file
            trans_from = aBank.userList.get(listId).accountList.get(i).acNumber;

            temp = editText.getText().toString();
            trans_to = searchAccount(temp);

            trans_money = String.valueOf(money);
            trans_message = editText2.getText().toString();

            // checks if there's enough moneyu on account
            if (aBank.userList.get(listId).accountList.get(i).getDebit() < money) {
                Toast.makeText(this, "Not enough money!", Toast.LENGTH_LONG).show();
            }
            else {
                //Withdraws money from the account and writes to user's transaction history file
                Toast.makeText(this, "Payment ont the way!", Toast.LENGTH_LONG).show();
                aBank.userList.get(listId).accountList.get(i).transaction.writeJSONFile(trans_filename, trans_name, trans_from, trans_to, trans_money, trans_message);
                aBank.userList.get(listId).accountList.get(i).withDraw(money);
                if(k>=0 && h>=0) {
                    //Adds money to target account and writes to it's transaction history

                    aBank.userList.get(k).accountList.get(h).deposit(money);
                    aBank.userList.get(k).accountList.get(h).transaction.writeJSONFile(aBank.userList.get(k).getName() + "_history.json", aBank.userList.get(k).getName(), trans_from, trans_to, trans_money, trans_message);
                }
            }
            updateText();
        }
    }
    //makes payment when "pey with credit" button is clicked
    public void payOnCreditClick(View v) throws JSONException {
        //temp variable
        String temp;
        //checks if account is disabled
        if(aBank.userList.get(listId).accountList.get(i).disabled == 1){
            Toast.makeText(this,"The account has been disabled",Toast.LENGTH_SHORT).show();
        }
        //checks if there's money selected on seekbar
        else if (money == 0){
            Toast.makeText(this,"Select more money",Toast.LENGTH_SHORT).show();
        }
        else {

            trans_from = aBank.userList.get(listId).accountList.get(i).acNumber;

            temp = editText.getText().toString();
            trans_to = searchAccount(temp);

            trans_money = String.valueOf(money);
            trans_message = editText2.getText().toString();

            if (aBank.userList.get(listId).accountList.get(i).getCredit() < money) {
                Toast.makeText(this, "Not enough credit!", Toast.LENGTH_LONG).show();
            }
           else {
                Toast.makeText(this, "Payment on the way!", Toast.LENGTH_LONG).show();
                aBank.userList.get(listId).accountList.get(i).transaction.writeJSONFile(trans_filename, trans_name, trans_from, trans_to, trans_money, trans_message);
                aBank.userList.get(listId).accountList.get(i).withDrawCredit(money);
                if(k>=0 && h>=0) {

                    aBank.userList.get(k).accountList.get(h).deposit(money);
                    aBank.userList.get(k).accountList.get(h).transaction.writeJSONFile(aBank.userList.get(k).getName() + "_history.json", aBank.userList.get(k).getName(), trans_from, trans_to, trans_money, trans_message);
                }
            }
            updateText();
        }
    }

    //Searches if the writed account is found from the accountlist and returns it's account number
    public String searchAccount(String ac_num){
        //Iterates through all users and accounts
        for(int i=0; i<aBank.userList.size();i++){
            for(int j=0; j<aBank.userList.get(i).accountList.size();j++){
                //checks if account is equal to found account number
                if ( aBank.userList.get(i).accountList.get(j).acNumber.equals(ac_num)){
                    //helper variables stores right user and account indexes
                    k = i;
                    h = j;
                    //returns found destination account number
                    return aBank.userList.get(i).accountList.get(j).getAcNumber();

                }
            }
        }
        return ac_num;
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
