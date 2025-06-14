package com.project.bloodbridge.controller;

import com.project.bloodbridge.model.BloodRequest;
import com.project.bloodbridge.repository.BloodRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/bloodrequests")
@CrossOrigin(origins = "*") // Enable CORS for frontend requests
public class BloodRequestController {

    @Autowired
    private BloodRequestRepository bloodRequestRepository;
    private final String UPLOAD_DIR = "C:/bloodbridge/uploads/";

    // Get all blood requests (Admin)
    @GetMapping
    public List<BloodRequest> getAllRequests() {
        return bloodRequestRepository.findAll();
    }
    
    @GetMapping("/history")
    public ResponseEntity<List<BloodRequest>> getFullRequestHistory() {
        List<BloodRequest> requests = bloodRequestRepository.findAll();
        requests.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));
        return ResponseEntity.ok(requests);
    }

    // Get blood request by ID (Admin)
    @GetMapping("/{id}")
    public ResponseEntity<BloodRequest> getRequestById(@PathVariable Long id) {
        return bloodRequestRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Update blood request details (Admin)
    @PutMapping("/{id}")
    public ResponseEntity<BloodRequest> updateRequest(@PathVariable Long id, @RequestBody BloodRequest requestDetails) {
        return bloodRequestRepository.findById(id).map(request -> {
            request.setPatientName(requestDetails.getPatientName());
            request.setPatientAge(requestDetails.getPatientAge());
            request.setReason(requestDetails.getReason());
            request.setBloodGroup(requestDetails.getBloodGroup());
            request.setUnit(requestDetails.getUnit());
            request.setPrescriptionPath(requestDetails.getPrescriptionPath());
            request.setDate(requestDetails.getDate());
            request.setRequesterId(requestDetails.getRequesterId());
            request.setRequesterType(requestDetails.getRequesterType());
            return ResponseEntity.ok(bloodRequestRepository.save(request));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete blood request by ID (Admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRequest(@PathVariable Long id) {
        return bloodRequestRepository.findById(id).map(request -> {
            bloodRequestRepository.delete(request);
            return ResponseEntity.ok("Blood request deleted");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found"));
    }

    // Update blood request status (Admin)
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        return bloodRequestRepository.findById(id).map(req -> {
            req.setStatus(payload.get("status")); // "Approved" or "Rejected"
            bloodRequestRepository.save(req);
            return ResponseEntity.ok("Status updated");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found"));
    }

    // Delete all blood requests for a patient
    @DeleteMapping("/by-patient/{patientId}")
    public ResponseEntity<String> deleteRequestsByPatient(@PathVariable Long patientId) {
        List<BloodRequest> requests = bloodRequestRepository.findByRequesterTypeAndRequesterId("Patient", patientId);
        bloodRequestRepository.deleteAll(requests);
        return ResponseEntity.ok("All requests deleted for patient");
    }

    // Get patient request history
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<BloodRequest>> getPatientRequestHistory(@PathVariable Long patientId) {
        List<BloodRequest> requests = bloodRequestRepository.findByRequesterTypeAndRequesterId("Patient", patientId);
        requests.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate())); // Sort by newest date
        return ResponseEntity.ok(requests);
    }


    // Donor: Make a new request
    @PostMapping("/by-donor")
    public ResponseEntity<?> makeDonorBloodRequest(@RequestBody BloodRequest request) {
        if (request.getRequesterId() == null) {
            return ResponseEntity.badRequest().body("Donor ID is required");
        }
        request.setRequesterType("Donor");
        request.setDate(LocalDate.now());
        request.setStatus("Pending");
        return ResponseEntity.ok(bloodRequestRepository.save(request));
    }

    // Donor: Get request history
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<BloodRequest>> getDonorRequestHistory(@PathVariable Long donorId) {
        List<BloodRequest> requests = bloodRequestRepository.findByRequesterTypeAndRequesterId("Donor", donorId);
        requests.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate())); // Sort by newest date
        return ResponseEntity.ok(requests);
    }


    // Patient: Make a new request
    @PostMapping("/by-patient")
    public ResponseEntity<?> makePatientBloodRequest(@RequestBody BloodRequest request) {
        if (request.getRequesterId() == null) {
            return ResponseEntity.badRequest().body("Patient ID is required");
        }
        request.setRequesterType("Patient");
        request.setDate(LocalDate.now());
        request.setStatus("Pending");
        return ResponseEntity.ok(bloodRequestRepository.save(request));
    }

    // Upload blood request with prescription
    @PostMapping("/upload")
    public ResponseEntity<?> uploadBloodRequestWithPrescription(
            @RequestParam String patientName,
            @RequestParam int patientAge,
            @RequestParam String reason,
            @RequestParam String bloodGroup,
            @RequestParam int unit,
            @RequestParam Long requesterId,
            @RequestParam String requesterType,
            @RequestParam(required = false) MultipartFile prescription
    ) {
        try {
            BloodRequest request = new BloodRequest();
            request.setPatientName(patientName);
            request.setPatientAge(patientAge);
            request.setReason(reason);
            request.setBloodGroup(bloodGroup);
            request.setUnit(unit);
            request.setRequesterId(requesterId);
            request.setRequesterType(requesterType);
            request.setDate(LocalDate.now());
            request.setStatus("Pending");

            if (prescription != null && !prescription.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + prescription.getOriginalFilename();
                String uploadDir = UPLOAD_DIR;
                File saveFile = new File(uploadDir + fileName);
                saveFile.getParentFile().mkdirs(); // ensure directory exists
                prescription.transferTo(saveFile);
                request.setPrescriptionPath(fileName);
            }

            return ResponseEntity.ok(bloodRequestRepository.save(request));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload prescription.");
        }
    }
    
    @GetMapping("/patient/{patientId}/stats")
    public ResponseEntity<Map<String, Long>> getPatientRequestStats(@PathVariable Long patientId) {
        Map<String, Long> stats = Map.of(
            "total", bloodRequestRepository.countByRequesterTypeAndRequesterId("Patient", patientId),
            "pending", bloodRequestRepository.countByRequesterTypeAndRequesterIdAndStatus("Patient", patientId, "Pending"),
            "approved", bloodRequestRepository.countByRequesterTypeAndRequesterIdAndStatus("Patient", patientId, "Approved"),
            "rejected", bloodRequestRepository.countByRequesterTypeAndRequesterIdAndStatus("Patient", patientId, "Rejected")
        );
        return ResponseEntity.ok(stats);
    }
    
  
}
