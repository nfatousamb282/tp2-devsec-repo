package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        // Créer ou renouveler la session
        HttpSession oldSession = req.getSession(false);

        if (oldSession != null) {
            System.out.println("LoginFilter: Invalidating old session");
            oldSession.invalidate();
        }

        // Créer une nouvelle session (ceci va déclencher CsrfSessionListener)
        HttpSession newSession = req.getSession(true);
        System.out.println("LoginFilter: New session created with ID: " + newSession.getId());

        // Vérifier que le token CSRF a été créé
        String csrfToken = (String) newSession.getAttribute("CSRF_TOKEN");
        System.out.println("LoginFilter: CSRF Token in session: " +
                (csrfToken != null ? csrfToken.substring(0, 10) + "..." : "null"));

        chain.doFilter(request, response);
    }
}