package com.fss.core.fssCalculation.controller;

import com.fss.core.fssCalculation.persistance.SubscriptionPlanRepository;
import com.fss.core.fssCalculation.persistance.UserRepository;
import com.fss.core.fssCalculation.securityconfig.SubscriptionPlan;
import com.fss.core.fssCalculation.securityconfig.User;
import com.fss.core.fssCalculation.service.payment.PaymentService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class paymentsController {

    @Value("${razorpay.key-secret}")
    private String secretKey;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionPlanRepository planRepository;

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public String paymentsPage(Model model) {

        Double goldPrice = 100.0; //planService.getPriceByPlanName("Gold");
        Double freePrice = 0.0;   //planService.getPriceByPlanName("Free");

        model.addAttribute("goldPrice", goldPrice);
        model.addAttribute("freePrice", freePrice);


        return "payments";
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam("planId") Long planId, @RequestParam(value = "duration", required = false, defaultValue = "1") int duration, Model model) throws RazorpayException {
        // Fetch plan details from DB
        SubscriptionPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan ID"));


        if ("Free".equalsIgnoreCase(plan.getPlanname())) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User user = userRepository.findByUsername(username).orElse(null);

            if (user == null) {
                return "redirect:/login?error=user_not_found";

            }
//            if (user.getSubscriptionEnd() != null) {
//                model.addAttribute("comment", "You have used your Free Quota, Please upgrade to Gold plan");
//                return "payments";
//            }
            if (!user.isSubscribed()) {

                user.setSubscriptionPlan(plan);
                user.setSubscriptionStart(LocalDate.now());
                user.setSubscriptionEnd(LocalDate.now().plusDays(20));
                userRepository.save(user);
            }

            return "redirect:/window/ui";

        }

        double totalAmount = plan.getPrice() * duration;
        // Create Razorpay order through service
        Map<String, Object> orderDetails = paymentService.createOrder(totalAmount, "");

        model.addAttribute("plan", plan);
        model.addAttribute("order", orderDetails);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("duration", duration);

        return "payment-page"; // -> src/main/resources/templates/payment-page.html
    }

    @GetMapping("/success")
    public String handlePaymentSuccess(
            @RequestParam("razorpay_payment_id") String paymentId,
            @RequestParam("razorpay_order_id") String orderId,
            @RequestParam("razorpay_signature") String signature,
            @RequestParam(value = "planId", required = false) Long planId,
            @RequestParam(value = "duration", required = false, defaultValue = "1") int duration,
            Model model) {

        boolean isValid = verifySignature(orderId, paymentId, signature);

        // TEMP — force true for local testing (optional)
        isValid = true;

        if (isValid) {
            // ✅ Signature verified successfully
            model.addAttribute("status", "success");
            model.addAttribute("message", "Payment successful!");
            model.addAttribute("paymentId", paymentId);
            model.addAttribute("orderId", orderId);

            try {
                // Get current logged-in user
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();
                User user = userRepository.findByUsername(username).orElse(null);

                if (user == null) {
                    model.addAttribute("message", "User not found!");
                    return "payment_failed";
                }

                // Get subscription plan details
                SubscriptionPlan plan = planRepository.findById(planId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid plan ID"));

                // Update user subscription
                user.setSubscriptionPlan(plan);
                user.setSubscriptionStart(LocalDate.now());
                LocalDate existingSubscriptionEndDate = user.getSubscriptionEnd();

                if(!user.isSubscribed())
                {
                    user.setSubscriptionEnd(LocalDate.now().plusMonths(duration));
                }
                else
                {
                    user.setSubscriptionEnd(existingSubscriptionEndDate.plusMonths(duration));// if already subscribed then added extended period
                }

                userRepository.save(user);

                // You can also log/save payment details to DB
                // paymentService.savePayment(orderId, paymentId, plan, user);

                model.addAttribute("planName", plan.getPlanname());
                model.addAttribute("subscriptionEnd", user.getSubscriptionEnd());

            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("message", "Error while updating subscription details!");
                return "payment_failed";
            }

            return "payment_success"; // ✅ Success page
        } else {
            // ❌ Signature mismatch — possible tampering
            model.addAttribute("status", "failed");
            model.addAttribute("message", "Payment verification failed!");
            return "payment_failed";
        }
    }


    /**
     * Helper method to verify the Razorpay signature.
     */
    private boolean verifySignature(String orderId, String paymentId, String signature) {
        try {
            String payload = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(), "HmacSHA256"));
            byte[] digest = mac.doFinal(payload.getBytes());
            String generatedSignature = new String(Base64.getEncoder().encode(digest));
            return generatedSignature.trim().equals(signature.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
