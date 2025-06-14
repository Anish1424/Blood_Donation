package com.project.bloodbridge.repository;

import com.project.bloodbridge.model.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findByEmailAndOtp(String email, String otp);
}
