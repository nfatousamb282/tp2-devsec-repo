package security.exceptions;

import model.User;

/**
*
* @author ouziri
* @version 0.1
*/

public class UserAthorizationException extends Exception {
	
	private User user;

	public UserAthorizationException(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}

}
