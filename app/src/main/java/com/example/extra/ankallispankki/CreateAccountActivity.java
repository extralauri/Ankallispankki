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
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateAccountActivity extends MainActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Variables for spinners and methods
    Integer setDebit=0,setCredit=0,listId, i=0, spinner1Selection=0, spinner2Selection=0;
    String text;
    //UI variables
    SeekBar newDebit, newCredit;
    Button addDebitAccount, addCreditAccount, deleteAccount;
    Spinner spinner,spinner2;
    Switch toggleswitch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
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
        newDebit = findViewById(R.id.seekBar);
        newCredit = findViewById(R.id.seekBar2);

        newDebit.setMax(10000);
        newCredit.setMax(1000);

       addSpinner();

        addDebitAccount = findViewById(R.id.button);
        addCreditAccount = findViewById(R.id.button2);
        deleteAccount = findViewById(R.id.button12);

        toggleswitch = findViewById(R.id.switch1);

        checkIfDisabled();

        toggleswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aBank.userList.get(listId).accountList.get(i).changeState();
            }
        });

        newDebit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                Toast.makeText(getApplicationContext(),"Debit:  "+progress, Toast.LENGTH_SHORT).show();
                setDebit = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                Toast.makeText(getApplicationContext(),"Debit: 0", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(getApplicationContext(),"Lisää rahaa:  "+progress, Toast.LENGTH_SHORT).show();
            }
        });

        newCredit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                Toast.makeText(getApplicationContext(),"Credit:  "+progress, Toast.LENGTH_SHORT).show();
                setCredit = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                Toast.makeText(getApplicationContext(),"Credit: 0", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(getApplicationContext(),"Lisää rahaa:  "+progress, Toast.LENGTH_SHORT).show();
            }
        });
    }
    // checks if account''s disabled variable is false
    public void checkIfDisabled(){
        if(aBank.userList.get(spinner1Selection).accountList.get(spinner2Selection).disabled == 0){
            toggleswitch.setChecked(false);
        }
        else {
            toggleswitch.setChecked(true);
        }
    }

    //spinner for selecting users
    public void addSpinner(){
        spinner = findViewById(R.id.spinner3);
        final List<String> list = new ArrayList<String>();

        //populates list with usernames
        for(int j=0;j<aBank.userList.size();j++){
            list.add(aBank.userList.get(j).getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner1Selection = position;
                addSpinner2();  // updates second spinner
                checkIfDisabled();  // updates toggle switch
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    // Spinner for selecting accounts
    public void addSpinner2(){
        spinner2 = findViewById(R.id.spinner7);
        final List<String> list2 = new ArrayList<String>();

        //populates list with account numbers
        for(int j=0;j<aBank.userList.get(spinner1Selection).accountList.size();j++){
            list2.add(aBank.userList.get(spinner1Selection).accountList.get(j).acNumber);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list2);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner2Selection = position;
                checkIfDisabled(); // updates toggle switch
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    // Creates new debit account with random account number
    public void setAddDebitAccount(View v){
        int number = (int)(Math.random()*1000000);
        aBank.userList.get(spinner1Selection).accountList.add(new debitAccount(String.valueOf(number),setDebit));
        updateSpinners();
    }

    // Creates new credit account with random account number
    public void setAddCreditAccount(View v){
        int number = (int)(Math.random()*1000000);
        aBank.userList.get(spinner1Selection).accountList.add(new creditAccount(String.valueOf(number),setDebit, setCredit));
        updateSpinners();
    }

    //Removes account based on spinner selection
    public void setDeleteAccount(View v){
        aBank.userList.get(spinner1Selection).accountList.remove((int)spinner2Selection);
        updateSpinners();
    }

    //Updates spinner values
    public void updateSpinners(){
       addSpinner();
       addSpinner2();
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
