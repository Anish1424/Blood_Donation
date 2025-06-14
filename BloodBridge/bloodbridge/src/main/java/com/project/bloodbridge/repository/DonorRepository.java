package com.project.bloodbridge.repository;

import com.project.bloodbridge.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DonorRepository extends JpaRepository<Donor, Long> {
    boolean existsByEmail(String email);
    Optional<Donor> findByEmail(String email);
}
