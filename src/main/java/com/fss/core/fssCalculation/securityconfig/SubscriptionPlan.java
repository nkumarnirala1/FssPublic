package com.fss.core.fssCalculation.securityconfig;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "subscriptionplan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String planname; // Free, Gold, Platinum
    private Double price; // 0.0, 499.0, 999.0
    private String duration; // Monthly, Yearly
}
