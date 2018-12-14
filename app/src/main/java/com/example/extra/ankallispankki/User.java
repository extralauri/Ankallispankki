package com.example.extra.ankallispankki;

import java.util.ArrayList;

public class User {

    private String name;
    private String address;
    private Integer age;
    private Integer ac_id;
    private String passwd;
    private String city;
    ArrayList<Account> accountList = new ArrayList<>();

    //User constractor
    public User(String username, String password, String homeaddress, String homecity, Integer userage, Integer id){
        //random value for account number
        Integer temp = (int)(Math.random()*1000000);
        String number = temp.toString();

        name = username;
        address = homeaddress;
        age = userage;
        ac_id = id;
        passwd = password;
        city = homecity;
        accountList.add(new debitAccount(number,1.0));
    }
    // Gets and Sets for variables
    public String getName(){return name;}
    public String getAddress(){return address;}
    public String getCity(){return city;}
    public String getPasswd(){return passwd;}
    public Integer getAge(){return age;}
    public Integer getID(){return ac_id;}
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setAge(Integer age){this.age = age;}
    public void setPasswd(String passwd){this.passwd = passwd;}
    public void setCity(String city){this.city = city;}

}



