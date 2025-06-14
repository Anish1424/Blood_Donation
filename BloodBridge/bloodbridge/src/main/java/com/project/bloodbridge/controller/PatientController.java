package com.project.bloodbridge.controller;

import com.project.bloodbridge.model.Patient;
import com.project.bloodbridge.repository.PatientRepository;
import com.project.bloodbridge.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // Patient Registration
    @PostMapping("/register")
    public ResponseEntity<String> registerPatient(@RequestBody Patient patient) {
        if (patientRepository.existsByEmail(patient.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already registered");
        }
        // Encrypt password before saving
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patientRepository.save(patient);
        return ResponseEntity.ok("Patient registered successfully");
    }

    // Patient Login
    @PostMapping("/login")
    public ResponseEntity<String> loginPatient(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            // Check encrypted password match
            if (passwordEncoder.matches(password, patient.getPassword())) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(401).body("Invalid password");
            }
        } else {
            return ResponseEntity.status(404).body("Patient not found");
        }
    }

    // Step 1: Generate and send OTP
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            String otp = String.format("%06d", new Random().nextInt(999999));
            LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

            patient.setOtp(otp);
            patient.setOtpRequestedTime(expiryTime);
            patientRepository.save(patient);

            emailService.sendOtpEmail(email, otp);
            return ResponseEntity.ok("OTP sent to email");
        } else {
            return ResponseEntity.status(404).body("Patient not found");
        }
    }

    // Step 2: Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String otp = payload.get("otp");

        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            if (patient.getOtp() != null && patient.getOtpRequestedTime() != null &&
                    patient.getOtp().equals(otp) &&
                    patient.getOtpRequestedTime().isAfter(LocalDateTime.now())) {
                return ResponseEntity.ok("OTP verified successfully");
            } else {
                return ResponseEntity.status(400).body("Invalid or expired OTP");
            }
        } else {
            return ResponseEntity.status(404).body("Patient not found");
        }
    }

    // Step 3: Update Password after OTP verified
    @PutMapping("/forgot-password")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String newPassword = payload.get("newPassword");

        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            // Encrypt new password before saving
            patient.setPassword(passwordEncoder.encode(newPassword));
            // Clear OTP fields after password reset
            patient.setOtp(null);
            patient.setOtpRequestedTime(null);
            patientRepository.save(patient);
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.status(404).body("Patient not found");
        }
    }
    
 // Get all patients (For admin panel)
    @GetMapping
    public ResponseEntity<?> getAllPatients() {
        return ResponseEntity.ok(patientRepository.findAll());
    }

}