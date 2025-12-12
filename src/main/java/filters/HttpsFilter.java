package filters;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest &&
                response instanceof HttpServletResponse httpResponse) {

            if (httpRequest.isSecure()) {
                // Requête HTTPS : on continue normalement
                chain.doFilter(request, response);
            } else {
                // Requête HTTP non sécurisée : on bloque et renvoie un message
                httpResponse.setContentType("text/html");
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
                httpResponse.getWriter().write("<html><body><h2>Accès refusé : veuillez utiliser HTTPS.</h2></body></html>");
                httpResponse.getWriter().flush();
            }

        } else {
            // Pas une requête HTTP : on laisse passer
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
