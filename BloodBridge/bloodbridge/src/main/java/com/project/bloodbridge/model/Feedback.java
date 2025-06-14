
package com.project.bloodbridge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userType;
    private String overallExperience;
    private String comfortLevel;
    private String processClarity;
    private String futureDonation;

    @Column(columnDefinition = "TEXT")
    private String suggestions;

    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getOverallExperience() {
        return overallExperience;
    }

    public void setOverallExperience(String overallExperience) {
        this.overallExperience = overallExperience;
    }

    public String getComfortLevel() {
        return comfortLevel;
    }

    public void setComfortLevel(String comfortLevel) {
        this.comfortLevel = comfortLevel;
    }

    public String getProcessClarity() {
        return processClarity;
    }

    public void setProcessClarity(String processClarity) {
        this.processClarity = processClarity;
    }

    public String getFutureDonation() {
        return futureDonation;
    }

    public void setFutureDonation(String futureDonation) {
        this.futureDonation = futureDonation;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
