package com.fss.core.fssCalculation;

import jakarta.servlet.ServletContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SessionTimeoutLogger implements CommandLineRunner {

    private final ServletContext servletContext;

    public SessionTimeoutLogger(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void run(String... args) {
        int timeout = servletContext.getSessionTimeout(); // in minutes
        System.out.println("Configured session timeout = " + timeout + " minutes");
    }
}
