package com.fss.core.fssCalculation.controller;


import com.fss.core.fssCalculation.persistance.SubscriptionPlanRepository;
import com.fss.core.fssCalculation.persistance.UserRepository;
import com.fss.core.fssCalculation.securityconfig.SubscriptionPlan;
import com.fss.core.fssCalculation.securityconfig.User;
import com.fss.core.fssCalculation.service.payment.PaymentService;
import com.fss.core.fssCalculation.service.userservice.EmailService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Controller
public class SecurityController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SubscriptionPlanRepository planRepository;
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private  JavaMailSender mailSender;

    @Value("${admin.email}")
    private String adminEmail;

    @GetMapping({"/", "/home"})
    public String home(Model model, Principal principal) {

        // Todo DB call to fetch name , later on use session to avoid DB call
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String showForgotPasswordPage(@RequestParam String email, Model model) {
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "No user found with this email!");
            return "forgot-password";
        }

        User user = userOpt.get();

        // Generate OTP
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5)); // 5 min expiry
        userRepository.save(user);

        // Send OTP via email
        emailService.sendEmail(user.getEmail(), "Nextgen Facade account recovery Code", "Your OTP is: " + otp);

        // redirect to OTP page
        return "redirect:/validate-otp?email=" + email;
    }

    @GetMapping("/validate-otp")
    public String validateOtpPage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "validate-otp";
    }

    @PostMapping("/validate-otp")
    public String handleValidateOtp(@RequestParam String email,
                                    @RequestParam String otp,
                                    Model model) {
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Invalid request!");
            return "validate-otp";
        }

        User user = userOpt.get();

        if (user.getOtp() != null &&
                user.getOtp().equals(otp) &&
                user.getOtpExpiry().isAfter(LocalDateTime.now())) {

            // OTP is valid → redirect to reset password page
            return "redirect:/reset-password?email=" + email;
        }

        model.addAttribute("email", email);
        model.addAttribute("error", "Invalid or expired OTP!");
        return "validate-otp";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam String email,
                                      @RequestParam String password,
                                      Model model) {
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Invalid request!");
            return "reset-password";
        }

        User user = userOpt.get();
        user.setPassword(password); // 🔒 encode with BCryptPasswordEncoder in real case
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return "redirect:/login?resetSuccess";
    }


    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }


    @PostMapping("/register")
    public String handleRegistration(@ModelAttribute User user, Model model) throws RazorpayException {

        Optional<User> user1 = userRepository.findByEmail(user.getEmail());
        if (user1.isPresent()) {
            return "redirect:/register?error=alreadyRegistered";
        }

        user.setUsername(user.getEmail().substring(0, user.getEmail().indexOf("@")));
        user.setRole("user");

        userRepository.save(user);


        return "redirect:/login?registered=registrationSuccessfull";


    }

    @PostMapping("/sendInquiry")
    public String sendInquiry(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String details,
            RedirectAttributes redirectAttributes) {

        try {
            String subject = "New Inquiry from " + name;
            String body = String.format("""
                    You have received a new inquiry from your website:
                    
                    Name: %s
                    Email: %s
                    Phone: %s
                    
                    Project Details:
                    %s
                    
                    """, name, email, phone, details);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(email);

            mailSender.send(message);

            redirectAttributes.addFlashAttribute("message", "✅ Inquiry sent successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Failed to send inquiry. Please try again.");
            e.printStackTrace();
        }

        // Redirect back to home page or contact section
        return "redirect:/#contact";
    }
}
