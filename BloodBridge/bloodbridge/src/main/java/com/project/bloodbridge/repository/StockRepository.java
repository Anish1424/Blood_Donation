package com.project.bloodbridge.repository;

import com.project.bloodbridge.model.BloodStock;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<BloodStock, Long> {
	 Optional<BloodStock> findByBloodGroup(String bloodGroup);
}
