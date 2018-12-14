package com.example.extra.ankallispankki;

import java.util.ArrayList;

public class Bank {
    //Arraylist for users
    ArrayList<User> userList = new ArrayList<User>();

    //Singleton pattern
    private static Bank aBank = null;

    //There can be only one bank
    public static Bank getInstance() {
        if(aBank==null) {
            aBank = new Bank();
        }
        return aBank;
    }

    private Bank() {
        //adds two new users. one with admin rights
        userList.add(new User("Admin", "Test","Bank","Lpr", 1,1));
        userList.add(new User("Matti", "Teppo","Kotikatu 1","Lappeenranta", 20,0));
    }

}

