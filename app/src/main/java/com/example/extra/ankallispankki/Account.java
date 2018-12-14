package com.example.extra.ankallispankki;
//Account class
abstract class Account{
    protected String acNumber;
    protected double debit;
    protected double credit;
    // Sets variable disable to false.
    protected Integer disabled=0;

    //Creates new transaction object
    Transactions transaction = new Transactions();

    //returns
    public String getAcNumber(){
        return acNumber;
    }
    public Double getDebit(){
        return debit;
    }
    public Double getCredit(){
        return credit;
    }
    public Integer getDisabled(){return disabled; }

    // withdraw method
    public void withDraw(double money) {
        double newdebit = this.debit-money;
        this.debit = newdebit;

    }
    // method to disable account
    public void changeState(){
        if(disabled == 0){
            disabled = 1 ;
        }
        else{
            disabled = 0;
        }
    }
    // withdraws money from credit account
    public void withDrawCredit(double money) {
        double newcredit = this.credit - money;
        this.credit = newcredit;
    }
    // deposit money to account
    public void deposit(double money) {
        this.debit = this.debit+money;
    }

}

class debitAccount extends Account{
    public debitAccount(String ac_number, double debit) {
        this.acNumber = ac_number;
        this.debit = debit;
    }
}

class creditAccount extends Account {
    public creditAccount(String ac_number, double debit, double credit) {
        this.acNumber = ac_number;
        this.debit = debit;
        this.credit = credit;
    }
}