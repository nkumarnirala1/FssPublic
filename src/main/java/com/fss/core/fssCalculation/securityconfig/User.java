package com.fss.core.fssCalculation.securityconfig;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String fullname;

    private String password;

    private String email;

    private String role;

    private String otp;

    private LocalDateTime otpExpiry;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscriptionplan_id")
    private SubscriptionPlan subscriptionPlan;


    private LocalDate subscriptionStart;
    private LocalDate subscriptionEnd;
    private boolean active;

    public boolean isSubscribed() {
        if (subscriptionPlan == null) return false;
        if (subscriptionEnd == null) return false;
        return subscriptionEnd.isAfter(LocalDate.now());
    }

    public boolean hasPlan(String planName) {
        if (subscriptionPlan == null) return false;
        return subscriptionPlan.getPlanname().equalsIgnoreCase(planName);
    }
}
