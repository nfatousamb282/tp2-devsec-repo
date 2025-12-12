package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import model.Account;
import model.User;
import model.exceptions.UserNotFoundException;
import persistence.bankAccounts.JdbcAccountsPersistence;
import persistence.users.JdbcUsersPersistence;

/**
 *
 * @author ouziri
 * @version 0.1
 */

public class JdbcMySQLMainPersistence implements IPersistence {

	private String dbName = "bankdb";
	private String dbUserName= "bankuser";
	private String udbUserPassword="bankpass";

	private Connection conn;

	private JdbcAccountsPersistence accountPersistence;
	private JdbcUsersPersistence usersPersistence;

	public JdbcMySQLMainPersistence() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/"+dbName +"?serverTimezone=UTC&characterEncoding=utf8", dbUserName, udbUserPassword);
			this.usersPersistence = new JdbcUsersPersistence(conn);
			this.accountPersistence = new JdbcAccountsPersistence (conn);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Connection getDBConnection () {
		return conn;
	}

	@Override
	public Account save(Account account) throws Exception {
		return accountPersistence.save(account);
	}

	@Override
	public Account findAccountById(int accountId) throws Exception {
		return this.accountPersistence.findById(accountId);
	}

	@Override
	public List<Account> findAccountByOwnerEmail(String ownerEmail) throws SQLException {
		return this.accountPersistence.findByOwnerEmail(ownerEmail);
	}

	@Override
	public void updateBalance(int accountId, double amount) throws Exception {
		this.accountPersistence.updateBalance(accountId, amount);
	}

	@Override
	public User validateUserByEmailPassword(String ownerEmail, String pwd) throws Exception  {
		User user = usersPersistence.findUserByEmailAndPwd(ownerEmail, pwd);
		return user;
	}

	@Override
	public User findUserByEmail(String userEmail) throws Exception {
		return usersPersistence.findUserByEmail(userEmail);
	}
}
