package com.fss.core.fssCalculation.securityconfig;


import com.fss.core.fssCalculation.service.userservice.interceptor.SubscriptionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SubscriptionInterceptor subscriptionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(subscriptionInterceptor)
                .addPathPatterns("/home/**", "/window/**", "/unitized/**"); //calculation endpoints
    }
}

