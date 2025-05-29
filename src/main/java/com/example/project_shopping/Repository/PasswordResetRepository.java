package com.example.project_shopping.Repository;

import com.example.project_shopping.Entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Integer> {
    Optional<PasswordReset> findByEmail(String email);
    void deleteByEmail(String email);
}
