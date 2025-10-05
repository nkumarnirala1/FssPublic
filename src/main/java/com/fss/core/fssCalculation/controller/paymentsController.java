package com.fss.core.fssCalculation.controller;

import com.fss.core.fssCalculation.persistance.SubscriptionPlanRepository;
import com.fss.core.fssCalculation.persistance.UserRepository;
import com.fss.core.fssCalculation.securityconfig.SubscriptionPlan;
import com.fss.core.fssCalculation.securityconfig.User;
import com.fss.core.fssCalculation.service.payment.PaymentService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class paymentsController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionPlanRepository planRepository;

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public String paymentsPage() {
        SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
        subscriptionPlan.setId((long) 1.0);
        subscriptionPlan.setPlanname("test");
        subscriptionPlan.setDuration("Monthly");
        subscriptionPlan.setPrice(0.0);


        return "payments";
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam("planId") Long planId, Model model) throws RazorpayException {
        // Fetch plan details from DB
        SubscriptionPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan ID"));

        // Create Razorpay order through service
        Map<String, Object> orderDetails = paymentService.createOrder(plan.getPrice(), "");

        model.addAttribute("plan", plan);
        model.addAttribute("order", orderDetails);

        return "payment-page"; // -> src/main/resources/templates/payment-page.html
    }

    @PostMapping("/success")
    public String paymentSuccess(@RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam Long planId) {
        SubscriptionPlan plan = planRepository.findById(planId).orElseThrow();


        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setSubscriptionPlan(plan);
        user.setSubscriptionStart(LocalDate.now());
        user.setSubscriptionEnd(LocalDate.now().plusMonths(1));
        user.setActive(true);
        userRepository.save(user);


        return "success";
    }

}
