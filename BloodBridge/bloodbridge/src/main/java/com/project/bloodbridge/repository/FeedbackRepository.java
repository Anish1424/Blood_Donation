package com.project.bloodbridge.repository;

import com.project.bloodbridge.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // No extra methods needed yet; JpaRepository gives basic CRUD operations
}
