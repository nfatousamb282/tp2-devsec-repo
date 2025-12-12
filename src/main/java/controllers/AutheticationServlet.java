package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Bank;
import model.User;

import java.io.IOException;

import controllers.exceptions.LimiteNbRequestsExceededException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author ouziri
 * @version 0.1
 */

@WebServlet("/auth")
public class AutheticationServlet extends HttpServlet {

	private Bank bank = Bank.getInstance();		// classe du Modele MVC implémentant les spécifications fonctionnelles
	
	private static final int LIMITE = 3; 			// nombre de requêtes acceptées par intervalle de temps glissant (DELAI_SECONDES)
    private static final int DELAI_SECONDES = 60; // intervalle de temps de la limite du nombre de requêtes 
 
	@Override

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		String message = null;

		try {
			this.limiteRequetes(request, response);

			if ("login".equals(action)) {

				String username = request.getParameter("pUserEmail");
				String password = request.getParameter("pUserPwd");

				try {
					// Étape 1 : récupérer l'utilisateur par email uniquement
					User user = bank.findUserByEmail(username); // ⚠️ tu dois avoir cette méthode dans Bank

					if (user == null) {
						response.sendRedirect("./view/login_logout_form.jsp?messageType=error&message=Incorrect credentials!");
						return;
					}

					// Étape 2 : Vérifier le mot de passe haché avec BCrypt
					BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

					if (!encoder.matches(password, user.getPassword())) {
						response.sendRedirect("./view/login_logout_form.jsp?messageType=error&message=Incorrect credentials!");
						return;
					}

					// Étape 3 : Authentification OK → créer session + cookie
					HttpSession session = request.getSession(true);
					session.setAttribute("principal", user);

					Cookie roleCookie = new Cookie("ClientRole", user.isAdmin() ? "ADMIN" : "CLIENT");
					response.addCookie(roleCookie);

					message = "Successful connection!";
					response.sendRedirect("./index.jsp?messageType=success&message=" + message);

				} catch (Exception e) {
					throw new ServletException(e);
				}

			} else if ("logout".equals(action)) {

				HttpSession session = request.getSession(false);
				if (session != null) session.invalidate();

				Cookie roleCookie = new Cookie("ClientRole", null);
				roleCookie.setMaxAge(0);
				response.addCookie(roleCookie);

				message = "Successful logout!";
				response.sendRedirect("./index.jsp?messageType=success&message=" + message);
			}

		} catch (LimiteNbRequestsExceededException e) {
			response.sendRedirect("./index.jsp?messageType=error&message=" + e.getMessage());
		}
	}


	private void limiteRequetes(HttpServletRequest request, HttpServletResponse response) throws IOException, LimiteNbRequestsExceededException {
		int nbRequetes = 0;
        long debutDelai = System.currentTimeMillis();

        Cookie nbCookie = rechercherCookie(request, "nbRequetes1min");
        Cookie debutCookie = rechercherCookie(request, "debutDelai");

        if (nbCookie != null) {
            try { nbRequetes = Integer.parseInt(nbCookie.getValue()); } catch (Exception e) { nbRequetes = 0; }
        }
        if (debutCookie != null) {
            try { debutDelai = Long.parseLong(debutCookie.getValue()); } catch (Exception e) { debutDelai = System.currentTimeMillis(); }
        }

        if (debutCookie == null || (System.currentTimeMillis() - debutDelai) > DELAI_SECONDES * 1000) {
            debutDelai = System.currentTimeMillis();
            nbRequetes = 1;
        } else {
            nbRequetes++;
        }

        if (nbRequetes > LIMITE) {
            response.setStatus(429);
            
            String message = "Request limit has been reached (" + LIMITE + " requests/minute) !";
            throw new LimiteNbRequestsExceededException (message);
			//response.sendRedirect("./index.jsp?messageType=error&message="+message);
            //return;
        }
        Cookie newNbCookie = new Cookie("nbRequetes1min", String.valueOf(nbRequetes));
        newNbCookie.setMaxAge(DELAI_SECONDES);
        newNbCookie.setPath("/");

        Cookie newDebutCookie = new Cookie("debutDelai", String.valueOf(debutDelai));
        newDebutCookie.setMaxAge(DELAI_SECONDES);
        newDebutCookie.setPath("/");

        response.addCookie(newNbCookie);
        response.addCookie(newDebutCookie);

//        response.getWriter().write("Requête acceptée (" + nbRequetes + "/" + LIMITE + " pour cette minute)");
//        String message = "Limite de requêtes atteinte (" + LIMITE + " requêtes/minute) !";
//		response.sendRedirect("./index.jsp?messageType=error&message="+message);
	}
	
	private Cookie rechercherCookie(HttpServletRequest request, String cookieName) {
	    Cookie[] cookies = request.getCookies();
	    if (cookies == null)
	    	return null;
	    for (Cookie c : cookies) {
	    	if (c.getName().equals(cookieName))
	    		return c;
	    }
	    return null;
	}

}