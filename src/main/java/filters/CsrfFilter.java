package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

public class CsrfFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String method = request.getMethod();
        String uri = request.getRequestURI();

        // 🔹 CSRF uniquement sur méthodes sensibles
        if (!("POST".equalsIgnoreCase(method)
                || "PUT".equalsIgnoreCase(method)
                || "DELETE".equalsIgnoreCase(method))) {
            chain.doFilter(req, res);
            return;
        }

        // 🔹 Exclusions (login, logout, public)
        if (uri.contains("/auth")
                || uri.contains("/logout")
                || uri.contains("/test-db")) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String sessionToken = (String) session.getAttribute("CSRF_TOKEN");
        String requestToken = request.getParameter("csrfToken");

        // 🔹 Vérifications STRICTES
        if (sessionToken == null || sessionToken.isBlank()
                || requestToken == null || requestToken.isBlank()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Missing CSRF token");
            return;
        }

        if (!sessionToken.equals(requestToken)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }

        chain.doFilter(req, res);
    }
}
