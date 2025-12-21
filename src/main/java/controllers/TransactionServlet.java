package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Account;
import model.Bank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/transaction")
public class TransactionServlet extends HttpServlet {

    private Bank bank = Bank.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String accountIdStr = req.getParameter("pAccountId");
        String amountExpression = req.getParameter("pAmountExpression");

        System.out.println("=== Transaction Servlet ===");
        System.out.println("Account ID: " + accountIdStr);
        System.out.println("Amount Expression: " + amountExpression);

        try {
            // Validation des paramètres
            if (accountIdStr == null || accountIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Account ID is required");
            }

            if (amountExpression == null || amountExpression.trim().isEmpty()) {
                throw new IllegalArgumentException("Amount expression is required");
            }

            int accountId = Integer.parseInt(accountIdStr);

            // Effectuer la transaction
            bank.updateBankAccount(accountId, amountExpression);

            System.out.println("Transaction SUCCESS");

            // Récupérer le compte mis à jour
            Account account = bank.findAccountById(accountId);
            List<Account> accounts = new ArrayList<>();
            accounts.add(account);

            req.setAttribute("TransactionOK", true);
            req.setAttribute("accounts", accounts);
            req.setAttribute("pAccountId", accountIdStr);

            // Rediriger vers view_accounts.jsp
            req.getRequestDispatcher("/view/view_accounts.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            System.out.println("Transaction FAILED: Invalid account ID");
            req.setAttribute("TransactionOK", false);
            req.setAttribute("pAccountId", accountIdStr);
            req.setAttribute("errorMessage", "Invalid account ID format");

            // CORRECTION : Rediriger vers do_transaction.jsp
            req.getRequestDispatcher("/view/client/do_transaction.jsp").forward(req, resp);

        } catch (IllegalArgumentException e) {
            System.out.println("Transaction FAILED: " + e.getMessage());
            req.setAttribute("TransactionOK", false);
            req.setAttribute("pAccountId", accountIdStr);
            req.setAttribute("errorMessage", e.getMessage());

            // CORRECTION : Rediriger vers do_transaction.jsp
            req.getRequestDispatcher("/view/client/do_transaction.jsp").forward(req, resp);

        } catch (Exception e) {
            System.out.println("Transaction FAILED: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("TransactionOK", false);
            req.setAttribute("pAccountId", accountIdStr);
            req.setAttribute("errorMessage", "Transaction error: " + e.getMessage());

            // CORRECTION : Rediriger vers do_transaction.jsp
            req.getRequestDispatcher("/view/client/do_transaction.jsp").forward(req, resp);
        }
    }
}