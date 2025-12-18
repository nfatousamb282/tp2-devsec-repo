package model;

/**
*
* @author ouziri
* @version 0.1
*/

public class User {
	private String email;
	private String name;
	private String password;
	private String roles;

	// to database
	public User(String email, String name, String password, String roles) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.roles = roles;
	}

	// from database OU pour utilisateur anonyme
	public User(String email, String name, String roles) {
		this.email = email;
		this.name = name;
		this.password = null; // explicitement null pour les users sans mdp
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	public String getRoles() {
		return roles;
	}
	
	public boolean isAdmin() {
		return this.roles.toUpperCase().contains("ADMIN");
	}
}
