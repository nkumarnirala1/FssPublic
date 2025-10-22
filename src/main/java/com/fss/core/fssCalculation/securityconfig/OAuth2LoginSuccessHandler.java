package com.fss.core.fssCalculation.securityconfig;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    // Target URL after successful OAuth2 login
    private static final String TARGET_URL = "https://localhost:8443/window/ui";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // You can add additional logic here (logging, set cookie, session attributes, etc.)
        response.sendRedirect(TARGET_URL);
    }
}
