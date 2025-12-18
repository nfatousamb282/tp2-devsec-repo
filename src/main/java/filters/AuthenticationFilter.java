package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.util.List;

public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);

        // URLs publiques → laisser passer
        if (uri.contains("/view/login_logout_form.jsp")
                || uri.contains("/auth")
                || uri.contains("/styles")
                || uri.endsWith("/index.jsp")) {
            chain.doFilter(req, res);
            return;
        }

        // Utilisateur non authentifié
        if (session == null || session.getAttribute("principal") == null) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/view/login_logout_form.jsp?message=Authentification requise"
            );
            return;
        }

        // Utilisateur authentifié
        chain.doFilter(req, res);
    }
}
