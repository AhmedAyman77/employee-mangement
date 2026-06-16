package com.example.employee_mangement.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee_mangement.entities.PasswordResetToken;

public interface PasswordRestRepo extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findOneByToken(String token);
}
