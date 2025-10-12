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

        // If user is not logged in, continue (security handles auth)
        if (auth == null || auth.getName().equals("anonymousUser")) {
            return true;
        }

        // Check subscription
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            response.sendRedirect("/login?error=user_not_found");
            return false;
        }

        if (!user.isSubscribed()) {
            // Redirect to subscription page if not subscribed
            response.sendRedirect("/payment");
            return false;
        }

        return true;
    }
}
