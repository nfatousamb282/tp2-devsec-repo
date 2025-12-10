package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Account;
import model.Bank;
import model.User;
import model.exceptions.UserNotFoundException;
import security.Authentication;
import security.exceptions.UserAthenticationException;
import security.exceptions.UserAthorizationException;

import java.io.IOException;
import java.net.Authenticator;
import java.net.Authenticator.RequestorType;
import java.util.ArrayList;
import java.util.List;

/**
*
* @author ouziri
* @version 0.1
*/

@WebServlet("/CreateBankAccount")
public class CreateBankAccountServlet extends HttpServlet {
	
	private Bank bank;
	private Authentication authentication;
	private String authorizedRoles; 
	
	@Override
	public void init() throws ServletException {
		bank = Bank.getInstance();
		authentication = Authentication.getInstance ();
		this.authorizedRoles = getServletContext().getInitParameter("CreateBankAccount");
		System.out.println(this.authorizedRoles);
		super.init();
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			User user = authentication.getAuthenticatedUser(request);			
			authentication.getAuthorization (user, authorizedRoles);
			super.service(request, response);
		} catch (UserAthenticationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			request.setAttribute("error", "Vous n'êtes pas connecté !");	// petit probleme d'architecture (rôle) à corriger facultativement !
			request.getRequestDispatcher("./view/error.jsp").forward(request, response);
		} catch (UserAthorizationException e) {
			request.setAttribute("error", "Vous n'avez las autorisations requises !");
			request.getRequestDispatcher("./view/error.jsp").forward(request, response);
			//e.printStackTrace();
		}
	}

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String clientEmail = request.getParameter("pClientEmail");
        System.out.println(clientEmail);
        //resp.addCookie(new Cookie("q", "secret"));
        try {
            Account a = bank.createAccount(clientEmail);
            List<Account> accounts = new ArrayList<Account>();
            accounts.add(a);
			request.setAttribute("accounts", accounts);
        } catch (Exception e) {
        }
        request.getRequestDispatcher("./view/view_accounts.jsp").forward(request, response);
    }
}
