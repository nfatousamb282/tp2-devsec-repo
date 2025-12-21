package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import java.io.IOException;
import java.util.*;

public class XSSProtectionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();

        // NE PAS appliquer le filtre XSS pour les ressources statiques
        if (uri.contains("/styles/") ||
                uri.contains("/css/") ||
                uri.contains("/js/") ||
                uri.contains("/images/") ||
                uri.contains("/img/") ||
                uri.endsWith(".css") ||
                uri.endsWith(".js") ||
                uri.endsWith(".jpg") ||
                uri.endsWith(".jpeg") ||
                uri.endsWith(".png") ||
                uri.endsWith(".gif") ||
                uri.endsWith(".svg") ||
                uri.endsWith(".ico") ||
                uri.endsWith(".woff") ||
                uri.endsWith(".woff2") ||
                uri.endsWith(".ttf")) {

            System.out.println("XSS Filter: Skipping static resource " + uri);
            chain.doFilter(request, response);
            return;
        }

        System.out.println("XSS Filter: Processing " + uri);

        // Envelopper la requête avec notre wrapper de protection XSS
        XSSRequestWrapper wrappedRequest = new XSSRequestWrapper(httpRequest);

        chain.doFilter(wrappedRequest, response);
    }

    /**
     * Wrapper de requête HTTP qui nettoie tous les paramètres contre les attaques XSS
     * SAUF les champs sensibles comme les mots de passe, emails, tokens et expressions arithmétiques
     */
    private static class XSSRequestWrapper extends HttpServletRequestWrapper {

        private static final PolicyFactory POLICY =
                Sanitizers.FORMATTING.and(Sanitizers.LINKS);

        // Liste des paramètres à NE PAS nettoyer
        private static final Set<String> EXCLUDED_PARAMS = new HashSet<>(Arrays.asList(
                "pUserPwd",           // Mot de passe utilisateur
                "pPassword",          // Mot de passe création compte
                "password",           // Mot de passe générique
                "pUserEmail",         // Email utilisateur
                "pClientEmail",       // Email client
                "pOwnerEmail",        // Email propriétaire
                "email",              // Email générique
                "pAmountExpression",  // Expression arithmétique (IMPORTANT!)
                "pAccountId",         // ID de compte
                "csrfToken",          // Token CSRF
                "CSRF_TOKEN",         // Token CSRF (variant)
                "token",              // Token générique
                "action"              // Action
        ));

        public XSSRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);

            // Si le paramètre est null ou dans la liste d'exclusion, ne pas le nettoyer
            if (value == null || EXCLUDED_PARAMS.contains(name)) {
                return value;
            }

            String sanitized = POLICY.sanitize(value);

            // Debug : afficher si quelque chose a été modifié
            if (!value.equals(sanitized)) {
                System.out.println("XSS: Sanitized '" + name + "' from '" + value + "' to '" + sanitized + "'");
            }

            return sanitized;
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) {
                return null;
            }

            // Si le paramètre est dans la liste d'exclusion, retourner tel quel
            if (EXCLUDED_PARAMS.contains(name)) {
                return values;
            }

            String[] sanitizedValues = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                sanitizedValues[i] = values[i] == null ? null : POLICY.sanitize(values[i]);
            }
            return sanitizedValues;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> originalMap = super.getParameterMap();
            Map<String, String[]> sanitizedMap = new HashMap<>();

            for (Map.Entry<String, String[]> entry : originalMap.entrySet()) {
                String paramName = entry.getKey();
                String[] values = entry.getValue();

                // Si le paramètre est dans la liste d'exclusion, ne pas le nettoyer
                if (EXCLUDED_PARAMS.contains(paramName)) {
                    sanitizedMap.put(paramName, values);
                } else {
                    String[] sanitized = new String[values.length];
                    for (int i = 0; i < values.length; i++) {
                        sanitized[i] = values[i] == null ? null : POLICY.sanitize(values[i]);
                    }
                    sanitizedMap.put(paramName, sanitized);
                }
            }

            return sanitizedMap;
        }
    }
}