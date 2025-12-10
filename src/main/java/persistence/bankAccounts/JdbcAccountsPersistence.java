package persistence.bankAccounts;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Account;
import model.User;

/**
*
* @author ouziri
* @version 0.1
*/

public class JdbcAccountsPersistence {
    private final Connection conn;

    public JdbcAccountsPersistence(Connection conn) throws SQLException {
        this.conn = conn;
        //initSchema();
    }
  
    public void initSchema() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS Accounts ("
		            		+ "account_id INT AUTO_INCREMENT PRIMARY KEY," 
		            		+ "owner_email VARCHAR(100) NOT NULL REFERENCES Users (email),"
		            		+ "balance double(10,2) DEFAULT 0"
		            		+ ")"
		            );
        }
    }

    // VULNERABLE: SQL built by concatenation
    public Account save(Account a) throws Exception {
        String sql = "INSERT INTO accounts(owner_email) VALUES ('" + a.getOwner().getEmail() + "')";
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                int accountId = generatedKeys.getInt(1);
                a.setAccountId(accountId);
            }
            return a;
        }
    }

    public Account findById(int accountId) throws SQLException {
    	String sql = "SELECT * FROM accounts a INNER JOIN users u ON a.owner_email = u.email WHERE account_id = " + accountId;
        try {
        	Statement st = conn.createStatement();
        	ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
            	User owner = new User(rs.getString("u.email"),rs.getString("u.name"), rs.getString("u.roles")); 
            	Account a = new Account(rs.getInt("a.account_id"), owner, rs.getDouble("balance"));
            	return a;
            }            
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    public List<Account> findByOwnerEmail(String ownerEmail) throws SQLException {
    	List<Account> accounts = new ArrayList();
        String sql = "SELECT * FROM accounts a INNER JOIN users u ON a.owner_email = u.email WHERE a.owner_email = '" + ownerEmail + "'";
        try {
        	Statement st = conn.createStatement();
        	ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
            	User owner = new User(rs.getString("u.email"),rs.getString("u.name"), rs.getString("u.roles")); 
            	Account a = new Account(rs.getInt("a.account_id"), owner, rs.getDouble("balance"));
                 accounts.add(a);
            }
        }
        catch (Exception e) {}
        return accounts; 
    }

    public void updateBalance(int accountId, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance + " + amount + " WHERE account_id = " + accountId;
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        }
    }
}
