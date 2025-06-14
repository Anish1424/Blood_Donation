package com.project.bloodbridge.controller;

import com.project.bloodbridge.model.Feedback;
import com.project.bloodbridge.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/feedback")

public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // Save feedback from donor/patient
    @PostMapping
    public Feedback submitFeedback(@RequestBody Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    // Get all feedbacks for admin portal
    @GetMapping
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
    
    @DeleteMapping
    public ResponseEntity<String> deleteAllFeedbacks() {
    	feedbackRepository.deleteAll();
        return ResponseEntity.ok("All feedbacks deleted");
    }
}

