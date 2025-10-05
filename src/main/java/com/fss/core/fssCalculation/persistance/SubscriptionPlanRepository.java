package com.fss.core.fssCalculation.persistance;

import com.fss.core.fssCalculation.securityconfig.SubscriptionPlan;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.jpa.repository.JpaRepository;

@Persistent
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
}
