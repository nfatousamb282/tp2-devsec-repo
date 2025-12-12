package model;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import model.exceptions.UserNotFoundException;
import persistence.IPersistence;
import persistence.JdbcMySQLMainPersistence;

/**
*
* @author ouziri
* @version 0.1
*/

public class Bank {
	
	private IPersistence persistence = new JdbcMySQLMainPersistence();
	private static Bank _singleton = new Bank();
	
	private Bank() {}
	
	public static Bank getInstance() {
		return _singleton;
	}
	
	public Account createAccount(String ownerEmail) throws Exception {	
		if (ownerEmail == null || ownerEmail.trim().isEmpty())
			throw new IllegalArgumentException("Owner name is empty");
		User owner = this.findUserByEmail(ownerEmail);
		if (owner == null)
			throw new UserNotFoundException(ownerEmail);
		return this.persistence.save(new Account(owner));
	}
	
	public User findUserByEmail(String email) throws Exception {
		return this.persistence.findUserByEmail(email);
	}
	
	public Account findAccountById(int accountId) throws Exception {
		return this.persistence.findAccountById(accountId);
	}
	
	public List<Account> findAccountByOwnerEmail(String ownerEmail) throws Exception {
		return this.persistence.findAccountByOwnerEmail(ownerEmail);
	}

//	public User validateUserByEmailPassword(String ownerEmail, String pwd) throws Exception {
//		User user = this.persistence.findUserByEmailAndPwd(ownerEmail, pwd);
//		if (user == null)
//			throw new UserNotFoundException(ownerEmail);
//		return user;
//	}


	public void updateBankAccount(int accountId, String amountExpression) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		Number result = (Number) engine.eval(amountExpression);
	    double amount = result.doubleValue();
 		this.persistence.updateBalance(accountId, amount);
	}
}