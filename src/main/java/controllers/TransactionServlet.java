package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Account;
import model.Bank;
import persistence.bankAccounts.JdbcAccountsPersistence;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
*
* @author ouziri
* @version 0.1
*/

@WebServlet("/transaction")
public class TransactionServlet extends HttpServlet {
		
	private Bank bank = Bank.getInstance();
	
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {    	
    	int accountId= Integer.parseInt(req.getParameter("pAccountId"));
        String amountExpression = req.getParameter("pAmountExpression");        
        try {
            bank.updateBankAccount(accountId, amountExpression);
            req.setAttribute("TransactionOK", true);
            Account a = bank.findAccountById(accountId);
            List<Account> accounts = new ArrayList<Account>();
            accounts.add(a);
            req.setAttribute("accounts", accounts);
            req.getRequestDispatcher("./view/view_accounts.jsp").forward(req, resp);   
        }
        catch (Exception e) {
        	req.setAttribute("TransactionOK", false);
        } 
    }
}
