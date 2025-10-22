package com.fss.core.fssCalculation.service.userservice;

import com.fss.core.fssCalculation.persistance.UserRepository;
import com.fss.core.fssCalculation.securityconfig.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // delegate to default service to fetch user attributes from provider
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();

        // Google returns "email" and "name" (and sometimes "given_name", "family_name")
        String email = (String) attributes.get("email");
        String fullName = (String) attributes.get("name");
        if (email == null) {
            // fallback: some providers use other attribute names -- throw for now
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        // Create or update user in DB
        createOrUpdateUser(email, fullName);

        return oauth2User; // returning oauth2User so Spring continues authentication flow
    }

    private void createOrUpdateUser(String email, String fullName) {
        Optional<User> existingByEmail = userRepository.findByEmail(email);
        if (existingByEmail.isPresent()) {
            // Optionally update fullname/active if you want
            User u = existingByEmail.get();
            boolean changed = false;
            if (u.getFullname() == null && fullName != null) {
                u.setFullname(fullName);
                changed = true;
            }
            if (!u.isActive()) {
                u.setActive(true);
                changed = true;
            }
            if (changed) userRepository.save(u);
            return;
        }

        // New user: build username as part before '@'
        String localPart = email.split("@")[0];
        String usernameCandidate = localPart;
        int suffix = 0;
        while (userRepository.findByUsername(usernameCandidate).isPresent()) {
            suffix++;
            usernameCandidate = localPart + suffix; // resolves collisions
        }

        User newUser = new User();
        newUser.setUsername(usernameCandidate);
        newUser.setEmail(email);
        newUser.setFullname(fullName);
        newUser.setPassword(null);          // OAuth users have no local password
        newUser.setRole("USER");            // default role
        newUser.setActive(true);            // active by default
        newUser.setOtp(null);
        newUser.setOtpExpiry(null);
        newUser.setSubscriptionPlan(null);
        newUser.setSubscriptionStart(null);
        newUser.setSubscriptionEnd(null);

        userRepository.save(newUser);
    }
}