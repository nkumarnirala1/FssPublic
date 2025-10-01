package com.fss.core.fssCalculation.controller.utility;

public class paymentsController {
    @Controller
    public class PageController {
        @GetMapping("/payments")
        public String paymentsPage() {
            // returns the static resource at /static/payments.html
            return "forward:/payments.html";
        }
    }

}
