package com.project.bloodbridge.repository;

import com.project.bloodbridge.model.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {

    // Existing methods:
    List<BloodRequest> findByRequesterTypeAndRequesterId(String requesterType, Long requesterId);

	long countByStatus(String string);
	List<BloodRequest> findByStatus(String status);
	long countByRequesterTypeAndRequesterId(String requesterType, Long requesterId);
	long countByRequesterTypeAndRequesterIdAndStatus(String requesterType, Long requesterId, String status);
  
   
}
