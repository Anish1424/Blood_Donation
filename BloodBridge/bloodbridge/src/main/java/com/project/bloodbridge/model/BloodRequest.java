package com.project.bloodbridge.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "blood_request")  // ✅ updated to match your DB
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientName;
    private int patientAge;
    private String reason;
    private String bloodGroup;
    private int unit;
    private String status;
    private LocalDate date;
    private Long requesterId;
    private String requesterType;
    
    @Column(name = "prescription_path")
    private String prescriptionPath;
    

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public int getPatientAge() { return patientAge; }
    public void setPatientAge(int patientAge) { this.patientAge = patientAge; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public int getUnit() { return unit; }
    public void setUnit(int unit) { this.unit = unit; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Long getRequesterId() { return requesterId; }
    public void setRequesterId(Long requesterId) { this.requesterId = requesterId; }

    public String getRequesterType() { return requesterType; }
    public void setRequesterType(String requesterType) { this.requesterType = requesterType; }

    public String getPrescriptionPath() { return prescriptionPath; }
    public void setPrescriptionPath(String prescriptionPath) { this.prescriptionPath = prescriptionPath; }
}
