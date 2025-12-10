package security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.User;
import security.exceptions.UserAthenticationException;
import security.exceptions.UserAthorizationException;

/**
*
* @author ouziri
* @version 0.1
*/

public class Authentication {

	private static Authentication _singleton = new Authentication();

	private Authentication() {
	}

	public static Authentication getInstance() {
		return _singleton;
	}	

	public User getAuthenticatedUser(HttpServletRequest request) throws UserAthenticationException {
		HttpSession session = request.getSession(false);
		if (session == null ||  session.getAttribute("principal") == null)
			throw new UserAthenticationException ();
		return (User) session.getAttribute("principal");
	}

	public void getAuthorization (User user, String authrizedRoles) throws UserAthorizationException {
		String [] userRoles = user.getRoles().toUpperCase().split(",");
		for (String userRole : userRoles) {
			if (authrizedRoles.toUpperCase().contains(userRole))
				return ;
		}
		throw new UserAthorizationException (user);
	}
}
