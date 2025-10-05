package com.fss.core.fssCalculation.service.payment;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${razorpay.key-id}")   // match with YAML key
    private String keyId;

    @Value("${razorpay.key-secret}") // match with YAML key
    private String secretKey;


    public String getKeyId() {
        return keyId;
    }


    public Map<String, Object> createOrder(Double amount, String receiptEmail) throws RazorpayException {
        RazorpayClient client = new RazorpayClient(keyId, secretKey);


        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", 50000); // in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", receiptEmail + "_receipt");
        Map<String, Object> response = new HashMap<>();

        try {
            Order order = client.orders.create(orderRequest);


            response.put("id", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("key", keyId);
        } catch (Exception ex) {

            //TODO

        }

        return response;

    }
}
