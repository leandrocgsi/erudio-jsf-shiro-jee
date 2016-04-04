package br.com.erudio.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

import org.slf4j.MDC;
@WebFilter(filterName = "MDCLoggingFilter", urlPatterns = {"/*"})
public class MDCLoggingFilter implements Filter {

    private static final String REQUEST_URL = "requestURL";
    private static final String USERNAME = "username";
    private static final String METHOD = "method";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String requestURL = req.getRequestURL().toString();
        MDC.put(REQUEST_URL, requestURL);
        MDC.put(METHOD, ((HttpServletRequest) request).getMethod());

        final Principal userPrincipal = req.getUserPrincipal();
        if (userPrincipal != null) {
            MDC.put(USERNAME, userPrincipal.getName());
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_URL);
            MDC.remove(METHOD);
            MDC.remove(USERNAME);
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}