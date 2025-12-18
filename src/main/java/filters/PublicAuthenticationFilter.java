package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.User;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PublicAuthenticationFilter implements Filter {

    private List<String> publicUrls;

    @Override
    public void init(FilterConfig filterConfig) {
        String urls = filterConfig.getInitParameter("public-urls");
        publicUrls = Arrays.asList(urls.split(","));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);

        String path = req.getRequestURI();

        boolean isPublic = publicUrls.stream().anyMatch(path::contains);

        boolean isLoginPage = path.contains("login_logout_form.jsp");

        if (isPublic && !isLoginPage) {
            if (session == null) {
                session = req.getSession(true);
            }

            if (session.getAttribute("principal") == null) {
                session.setAttribute("principal",
                        new User("anonymous", "Anonymous", "ANONYMOUS"));
            }
        }


        chain.doFilter(request, response);
    }
}
