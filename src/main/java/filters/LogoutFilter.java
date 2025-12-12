package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 🔹 1. Invalider la session côté serveur
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 🔹 2. Supprimer tous les cookies, incluant JSESSIONID
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                c.setMaxAge(0);
                c.setValue("");
                c.setPath("/");  // nécessaire pour que la suppression fonctionne
                res.addCookie(c);
            }
        }

        // 🔹 3. Redirection après déconnexion
        res.sendRedirect(req.getContextPath() + "/index.jsp?message=Successful logout!");
    }

}
