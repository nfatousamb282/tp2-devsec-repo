package persistence.users;

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

public class JdbcUsersPersistence {
    private final Connection conn;

    public JdbcUsersPersistence(Connection conn) throws SQLException {
        this.conn = conn;
        //initSchema();
    }
  
    public void initSchema() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS Users ("
		            		+ "email VARCHAR(100) PRIMARY KEY," 
		            		+ "name VARCHAR(100) NOT NULL,"
		            		+ "password VARCHAR(100) NOT NULL,"
		            		+ "roles VARCHAR(20) NOT NULL"
		            		+ ")"
		            );
           
        }
    }

    public void save(User u) throws Exception {
        String sql = "INSERT INTO users VALUES ('" + u.getEmail() + "', "+u.getName() +"', '"+u.getPassword() + "')";
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public User findUserByEmailAndPwd(String email, String pwd) throws Exception {
        String sql = "SELECT email, name, roles FROM users WHERE email = '" + email + "' and password = '" + pwd + "'";
        
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
        	return new User(rs.getString("email"), rs.getString("name"), rs.getString("roles"));
        }
        return null;
    }

	public User findUserByEmail(String userEmail) throws Exception {
		String sql = "SELECT email, name, roles FROM users WHERE email = '" + userEmail + "'";
		Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
        	return new User(rs.getString("email"), rs.getString("name"), rs.getString("roles"));
        }
		return null;
	}
}
