package nh.graphql.braidexample.deliveryservice.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String authHeader = req.getHeader("delivery-auth-token");
        log.info("AuthHeader for '{}': '{}'",
            req.getRequestURI(),
            authHeader);

        chain.doFilter(request, response);
    }
}