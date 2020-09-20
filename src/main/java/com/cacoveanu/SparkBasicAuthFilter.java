package com.cacoveanu;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class SparkBasicAuthFilter implements Filter {

    private String username;
    private String password;
    private String realm;

    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain
    ) throws IOException, ServletException {
        Map<String, String> headers = getHeaders((HttpServletRequest) servletRequest);
        if (isAuthenticated(headers)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).setHeader(
                "WWW-Authenticate",
                "Basic realm=\"" + realm + "\""
            );
            ((HttpServletResponse) servletResponse).setStatus(401);
        }
    }

    private static Map<String, String> parseMap(
        String data,
        String entrySeparator,
        String keyValueSeparator
    ) {
        return Arrays.stream(data.split(entrySeparator))
                .map(p -> p.split(keyValueSeparator))
                .collect(Collectors.toMap(a -> a[0], a-> a[1]));
    }

    private Boolean isAuthenticated(Map<String, String> headers) {
        if (headers.containsKey("Authorization")) {
            String token = headers.get("Authorization");
            String userPassB64 = token.substring("Basic ".length());
            String userPassText = new String(
                DatatypeConverter.parseBase64Binary(userPassB64)
            );
            String username = userPassText.substring(
                0, 
                userPassText.indexOf(':')
            );
            String password = userPassText.substring(
                userPassText.indexOf(':') + 1
            );
            return this.username.equals(username)
                && this.password.equals(password);
        }
        return false;
    }

    private Map<String, String> getHeaders(HttpServletRequest servletRequest) {
        return Collections.list(servletRequest.getHeaderNames()).stream()
                .collect(Collectors.toMap(h -> h, servletRequest::getHeader));
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        username = filterConfig.getInitParameter("username");
        password = filterConfig.getInitParameter("password");
        realm = filterConfig.getInitParameter("realm");
    }

    public void destroy() {

    }
}