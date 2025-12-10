package model.exceptions;

/**
*
* @author ouziri
* @version 0.1
*/

public class UserNotFoundException extends Exception {

	public UserNotFoundException(String ownerEmail) {
		super (ownerEmail);
	}

}
