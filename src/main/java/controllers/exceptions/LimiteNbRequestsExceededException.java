package controllers.exceptions;

/**
*
* @author ouziri
* @version 0.1
*/

public class LimiteNbRequestsExceededException extends Exception {

	public LimiteNbRequestsExceededException(String message) {
		super (message);
	}

}
