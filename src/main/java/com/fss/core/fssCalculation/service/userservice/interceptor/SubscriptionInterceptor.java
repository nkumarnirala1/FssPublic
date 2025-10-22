package com.fss.core.fssCalculation.service.userservice.interceptor;

import com.fss.core.fssCalculation.persistance.UserRepository;
import com.fss.core.fssCalculation.securityconfig.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SubscriptionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("🚀 Interceptor hit for: " + request.getRequestURI());

        if (auth == null || auth.getPrincipal().equals("anonymousUser")) {
            return true;
        }

        String username = null;

        Object principal = auth.getPrincipal();

        // ✅ Case 1: Form Login (principal is org.springframework.security.core.userdetails.User)
        if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
            username = springUser.getUsername();  // already matches DB username
        }

        // ✅ Case 2: OAuth2 Login (principal is DefaultOAuth2User)
        else if (principal instanceof org.springframework.security.oauth2.core.user.DefaultOAuth2User oauthUser) {
            String email = (String) oauthUser.getAttributes().get("email");
            if (email != null) {
                // your usernames = part before @
                username = email.split("@")[0];
            }
        }

        if (username == null) {
            // Cannot map principal to DB user
            response.sendRedirect("/login?error=principal_not_mapped");
            return false;
        }

        // ------------------- Fetch DB user -------------------
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            response.sendRedirect("/login?error=user_not_found");
            return false;
        }

        // ------------------- Check subscription -------------------
        if (!user.isSubscribed()) {
            response.sendRedirect("/payment");
            return false;
        }

        return true;
    }

}
