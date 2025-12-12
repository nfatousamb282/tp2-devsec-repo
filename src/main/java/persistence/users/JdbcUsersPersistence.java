package persistence.users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPwd = encoder.encode(u.getPassword());
        u.setPassword(hashedPwd);

        String sql = "INSERT INTO users VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getName());
            ps.setString(3, u.getPassword()); // mot de passe haché
            ps.setString(4, u.getRoles());
            ps.executeUpdate();
        }
    }

    public User findUserByEmailAndPwd(String email, String pwd) throws Exception {
        User user = findUserByEmail(email);
        if (user == null) return null;

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(pwd, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }


    public User findUserByEmail(String userEmail) throws Exception {
        String sql = "SELECT email, name, password, roles FROM users WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("email"),
                            rs.getString("name"),
                            rs.getString("password"), // récupère le hash
                            rs.getString("roles")
                    );
                }
            }
        }
        return null;
    }
}
