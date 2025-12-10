package model;

/**
*
* @author ouziri
* @version 0.1
*/

public class Account {
    private int accountId;
    private User owner;
    private double balance;
    
    // to database
    public Account(User owner) {  	
        this.owner = owner;
        this.balance = 0.;
    }

    // from database
    public Account(int accountId, User owner, double balance) {
		this.accountId = accountId;
		this.owner = owner;
		this.balance = balance;
	}
    
	public int getAccountId() {
    	return accountId;
    }
	
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
    
    public User getOwner() {
		return owner;
	}
    
    public double getBalance() {
    	return balance;
    }

    public void credit(double amount) {
        this.balance = this.balance + amount;
    }

    public void debit(double amount) {
    	this.balance = this.balance + amount;
    }
}
