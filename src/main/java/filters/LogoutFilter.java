package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

public class LogoutFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                c.setValue("");
                c.setPath(request.getContextPath());
                c.setMaxAge(0);
                response.addCookie(c);
            }
        }

        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}
