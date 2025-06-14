package com.project.bloodbridge.repository;

import com.project.bloodbridge.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query("SELECT d FROM Donation d WHERE d.donor.email = :email")
    List<Donation> findByDonorEmail(@Param("email") String donorEmail);

    List<Donation> findByDonorId(Long donorId);

    void deleteByDonorId(Long donorId);
    long countByStatus(String status);

    @Query("SELECT SUM(d.unit) FROM Donation d WHERE d.status = :status")
    Long sumUnitsByStatus(@Param("status") String status);
}
