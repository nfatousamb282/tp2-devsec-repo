package model;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import model.exceptions.UserNotFoundException;
import persistence.IPersistence;
import persistence.JdbcMySQLMainPersistence;

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

	/**
	 * Sauvegarder un nouvel utilisateur
	 */



	public void updateBankAccount(int accountId, String amountExpression) throws Exception {
		System.out.println("Bank.updateBankAccount called");
		System.out.println("  Account ID: " + accountId);
		System.out.println("  Expression: " + amountExpression);

		// Validation : ne permettre que des nombres et opérateurs de base
		// IMPORTANT : Ne pas utiliser Encode.forJava() ici car le XSSFilter s'en charge déjà !
		String cleanExpression = amountExpression.trim();

		if (!cleanExpression.matches("[0-9+\\-*/().\\s]+")) {
			System.out.println("  VALIDATION FAILED: Invalid characters");
			throw new IllegalArgumentException("Expression arithmétique invalide. Seuls les nombres et opérateurs (+, -, *, /, parenthèses) sont autorisés.");
		}

		System.out.println("  Expression validated: " + cleanExpression);

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");

		if (engine == null) {
			throw new Exception("JavaScript engine not available");
		}

		try {
			Object result = engine.eval(cleanExpression);
			System.out.println("  Evaluation result: " + result);

			if (result == null) {
				throw new Exception("Expression evaluation returned null");
			}

			Number numResult = (Number) result;
			double amount = numResult.doubleValue();
			System.out.println("  Amount to add: " + amount);

			this.persistence.updateBalance(accountId, amount);
			System.out.println("  Balance updated successfully");

		} catch (Exception e) {
			System.out.println("  ERROR: " + e.getMessage());
			throw new Exception("Erreur lors de l'évaluation de l'expression: " + e.getMessage());
		}
	}
}