package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Account;
import model.Bank;
import model.User;
import security.Authentication;
import security.exceptions.UserAthenticationException;
import security.exceptions.UserAthorizationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
*
* @author ouziri
* @version 0.1
*/

@WebServlet("/viewAccount")
public class ViewAccountServlet extends HttpServlet {

	private Bank bank = Bank.getInstance();
	
	private Authentication authentication;
	private String authorizedRoles; 
	
	@Override
	public void init() throws ServletException {
		bank = Bank.getInstance();
		authentication = Authentication.getInstance ();
		this.authorizedRoles = getServletContext().getInitParameter("ViewAccount");
		super.init();
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			User user = authentication.getAuthenticatedUser(request);
			authentication.getAuthorization (user, authorizedRoles);
			super.service(request, response);
		} catch (UserAthenticationException e) {
			request.setAttribute("error", "Vous n'êtes pas connecté !");
			request.getRequestDispatcher("./view/error.jsp").forward(request, response);
		} catch (UserAthorizationException e) {
			request.setAttribute("error", "Vous n'avez pas las autorisations requises !");
			request.getRequestDispatcher("./view/error.jsp").forward(request, response);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String searchType = req.getParameter("searchBy");
		if (searchType.equals("serachById")) {
			int accountId= Integer.parseInt(req.getParameter("pAccountId"));
			try {
				Account a = bank.findAccountById(accountId);
				if (a != null) {
					List<Account> accounts = new ArrayList<Account>();
					accounts.add(a);
					req.setAttribute("accounts", accounts);
				}
			} catch (Exception e) {}
		}
		else {
			if (searchType.equals("serachByOwnerEmail")) {
				try {
					String ownerEmail= req.getParameter("pOwnerEmail");
					List<Account> accounts = bank.findAccountByOwnerEmail(ownerEmail);
					if (accounts.size() > 0)						
						req.setAttribute("accounts", accounts);
				}
				catch (Exception e) {}
			}
			else {	//recherche par owner email et account number/id
				try {	// défaut : critère de recherche réalisé dans la Servlet  
					int accountId= Integer.parseInt(req.getParameter("pAccountId"));
					String ownerEmail= req.getParameter("pOwnerEmail");
					Account a = bank.findAccountById(accountId);  
					if (a != null &&a.getOwner().getEmail().equals(ownerEmail)) {
						List<Account> accounts = new ArrayList<Account>();
						accounts.add(a);
						req.setAttribute("accounts", accounts);
					}
				}
				catch (Exception e) {}
			}	
		}
		req.getRequestDispatcher("./view/view_accounts.jsp").forward(req, resp);
	}
}
