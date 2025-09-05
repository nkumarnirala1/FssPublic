package com.fss.core.fssCalculation.persistance;

import com.fss.core.fssCalculation.securityconfig.User;

import org.springframework.data.annotation.Persistent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Persistent
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}