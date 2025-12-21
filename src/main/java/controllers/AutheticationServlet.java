package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Bank;
import model.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import controllers.exceptions.LimiteNbRequestsExceededException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Servlet d'authentification (login / logout)
 *
 * @author ouziri
 * @version 1.0
 */
@WebServlet("/auth")
public class AutheticationServlet extends HttpServlet {

	private Bank bank = Bank.getInstance();

	private static final int LIMITE = 3;            // nb max de requêtes
	private static final int DELAI_SECONDES = 60;   // fenêtre de temps

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		try {
			limiteRequetes(request, response);

			if ("login".equals(action)) {

				String username = request.getParameter("pUserEmail");
				String password = request.getParameter("pUserPwd");

				User user = bank.findUserByEmail(username);

				if (user == null) {
					redirectWithMessage(response,
							"./view/login_logout_form.jsp",
							"error",
							"Incorrect credentials!");
					return;
				}

				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

				if (!encoder.matches(password, user.getPassword())) {
					redirectWithMessage(response,
							"./view/login_logout_form.jsp",
							"error",
							"Incorrect credentials!");
					return;
				}

				HttpSession session = request.getSession(true);
				session.setAttribute("principal", user);

				Cookie roleCookie = new Cookie(
						"ClientRole",
						user.isAdmin() ? "ADMIN" : "CLIENT"
				);
				roleCookie.setHttpOnly(true);
				roleCookie.setSecure(true);
				roleCookie.setPath("/");
				roleCookie.setMaxAge(3600);

				response.addCookie(roleCookie);

				redirectWithMessage(response,
						"./index.jsp",
						"success",
						"Successful connection!");
				return;
			}


			if ("logout".equals(action)) {

				HttpSession session = request.getSession(false);
				if (session != null) {
					session.invalidate();
				}

				// Supprimer cookie rôle
				Cookie roleCookie = new Cookie("ClientRole", "");
				roleCookie.setMaxAge(0);
				roleCookie.setPath("/");
				response.addCookie(roleCookie);

				redirectWithMessage(response,
						"./index.jsp",
						"success",
						"Successful logout!");
				return;
			}

		} catch (Exception e) {

			redirectWithMessage(response,
					"./index.jsp",
					"error",
					e.getMessage());
		}
	}

	private void limiteRequetes(HttpServletRequest request, HttpServletResponse response)
			throws IOException, LimiteNbRequestsExceededException {

		int nbRequetes = 0;
		long debutDelai = System.currentTimeMillis();

		Cookie nbCookie = rechercherCookie(request, "nbRequetes1min");
		Cookie debutCookie = rechercherCookie(request, "debutDelai");

		if (nbCookie != null) {
			try { nbRequetes = Integer.parseInt(nbCookie.getValue()); }
			catch (Exception ignored) {}
		}

		if (debutCookie != null) {
			try { debutDelai = Long.parseLong(debutCookie.getValue()); }
			catch (Exception ignored) {}
		}

		if (debutCookie == null ||
				(System.currentTimeMillis() - debutDelai) > DELAI_SECONDES * 1000) {
			debutDelai = System.currentTimeMillis();
			nbRequetes = 1;
		} else {
			nbRequetes++;
		}

		if (nbRequetes > LIMITE) {
			throw new LimiteNbRequestsExceededException(
					"Request limit reached (" + LIMITE + " requests/minute)"
			);
		}

		Cookie newNbCookie = new Cookie("nbRequetes1min", String.valueOf(nbRequetes));
		newNbCookie.setMaxAge(DELAI_SECONDES);
		newNbCookie.setPath("/");

		Cookie newDebutCookie = new Cookie("debutDelai", String.valueOf(debutDelai));
		newDebutCookie.setMaxAge(DELAI_SECONDES);
		newDebutCookie.setPath("/");

		response.addCookie(newNbCookie);
		response.addCookie(newDebutCookie);
	}


	private Cookie rechercherCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) return null;

		for (Cookie c : cookies) {
			if (c.getName().equals(cookieName)) {
				return c;
			}
		}
		return null;
	}

	private void redirectWithMessage(HttpServletResponse response,
									 String page,
									 String type,
									 String message) throws IOException {

		String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
		response.sendRedirect(page +
				"?messageType=" + type +
				"&message=" + encodedMessage);
	}
}
