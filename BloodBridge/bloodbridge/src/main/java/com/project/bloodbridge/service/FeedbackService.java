package com.project.bloodbridge.service;

import com.project.bloodbridge.model.Feedback;
import com.project.bloodbridge.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // Save feedback submitted by donor/patient
    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    // Fetch all feedback to display to admin
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
}
