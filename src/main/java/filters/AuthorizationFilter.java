package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthorizationFilter implements Filter {

    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);

        // URLs publiques → pas de vérification d'autorisation
        if (uri.contains("/view/login_logout_form.jsp")
                || uri.contains("/auth")
                || uri.contains("/styles")
                || uri.contains("/test-db")
                || uri.contains("/view/error.jsp")
                || uri.endsWith("/index.jsp")
                || uri.endsWith("/")) {
            chain.doFilter(req, res);
            return;
        }

        // Récupérer l'utilisateur de la session
        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("principal");
        }

        // Si pas d'utilisateur ou utilisateur anonyme → déjà géré par AuthenticationFilter
        if (user == null || "ANONYMOUS".equals(user.getRoles())) {
            chain.doFilter(req, res);
            return;
        }

        // Vérifier les autorisations selon l'URL
        boolean isAuthorized = checkAuthorization(uri, user);

        if (!isAuthorized) {
            // Accès refusé
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendRedirect(
                    request.getContextPath()
                            + "/index.jsp?messageType=error&message=Access denied: insufficient permissions"
            );
            return;
        }

        // Autorisation OK → continuer
        chain.doFilter(req, res);
    }

    /**
     * Vérifie si l'utilisateur a les droits pour accéder à l'URI demandée
     */
    private boolean checkAuthorization(String uri, User user) {
        String userRole = user.getRoles().toUpperCase();

        System.out.println("=== Authorization Check ===");
        System.out.println("URI: " + uri);
        System.out.println("User: " + user.getEmail());
        System.out.println("Role: " + userRole);

        String requiredRolesParam = null;

        // Vérification des SERVLETS
        if (uri.contains("/CreateBankAccount")) {
            requiredRolesParam = context.getInitParameter("CreateBankAccount");
            System.out.println("Resource: CreateBankAccount Servlet");
        }
        else if (uri.contains("/viewAccount")) {
            requiredRolesParam = context.getInitParameter("ViewAccount");
            System.out.println("Resource: ViewAccount Servlet");
        }
        else if (uri.contains("/transaction")) {
            requiredRolesParam = context.getInitParameter("Transaction");
            System.out.println("Resource: Transaction Servlet");
        }
        // Vérification des PAGES JSP
        else if (uri.contains("/view/admin/")) {
            requiredRolesParam = context.getInitParameter("AdminPages");
            System.out.println("Resource: Admin Pages");
        }
        else if (uri.contains("/view/client/")) {
            requiredRolesParam = context.getInitParameter("ClientPages");
            System.out.println("Resource: Client Pages");
        }

        System.out.println("Required roles: " + requiredRolesParam);

        // Si aucune règle définie pour cette URI → accès autorisé par défaut
        if (requiredRolesParam == null || requiredRolesParam.trim().isEmpty()) {
            System.out.println("Result: AUTHORIZED (no rule defined)");
            System.out.println("===========================\n");
            return true;
        }

        // Parser les rôles autorisés (séparés par des virgules)
        List<String> requiredRoles = Arrays.stream(requiredRolesParam.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .toList();

        // Vérifier si l'utilisateur a un des rôles requis
        boolean authorized = requiredRoles.contains(userRole);
        System.out.println("Result: " + (authorized ? "AUTHORIZED ✓" : "DENIED ✗"));
        System.out.println("===========================\n");

        return authorized;
    }

    @Override
    public void destroy() {
        // Nettoyage si nécessaire
    }
}