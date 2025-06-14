package com.project.bloodbridge.controller;

import com.project.bloodbridge.model.Donor;
import com.project.bloodbridge.model.Donation;
import com.project.bloodbridge.repository.DonorRepository;
import com.project.bloodbridge.repository.DonationRepository;
import com.project.bloodbridge.service.EmailService;
import com.project.bloodbridge.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/donors")
public class DonorController {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private OtpService otpService;

    // ✅ Donor Registration
    @PostMapping("/register")
    public ResponseEntity<?> registerDonor(@RequestBody Donor donor) {
        if (donorRepository.existsByEmail(donor.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already registered");
        }
        donor.setPassword(passwordEncoder.encode(donor.getPassword()));
        donorRepository.save(donor);
        return ResponseEntity.ok("Donor registered successfully");
    }

    // ✅ Donor Login
    @PostMapping("/login")
    public ResponseEntity<?> loginDonor(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<Donor> optionalDonor = donorRepository.findByEmail(email);

        if (optionalDonor.isPresent() && passwordEncoder.matches(password, optionalDonor.get().getPassword())) {
            Donor donor = optionalDonor.get();
            Map<String, Object> response = new HashMap<>();
            response.put("donorId", donor.getId());
            response.put("fullName", donor.getFullName());
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }




    // ✅ Send OTP
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        Optional<Donor> donorOpt = donorRepository.findByEmail(email);
        if (donorOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Email not registered");
        }
        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);
        return ResponseEntity.ok("OTP sent to your registered email");
    }

    // ✅ Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        if (email == null || otp == null || email.isEmpty() || otp.isEmpty()) {
            return ResponseEntity.badRequest().body("Email and OTP are required");
        }
        boolean valid = otpService.verifyOtp(email, otp);
        if (valid) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.status(400).body("Invalid OTP");
        }
    }

    // ✅ Forgot Password
    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        if (email == null || newPassword == null || email.isEmpty() || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("Email and new password are required");
        }
        Optional<Donor> donorOpt = donorRepository.findByEmail(email);
        if (donorOpt.isPresent()) {
            Donor donor = donorOpt.get();
            donor.setPassword(passwordEncoder.encode(newPassword));
            donorRepository.save(donor);
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.status(404).body("Donor not found");
        }
    }

    // ✅ Admin: Get All Donors
    @GetMapping("/admin/all")
    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    // ✅ Admin: Delete Donor and Donations
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteDonor(@PathVariable Long id) {
        Optional<Donor> donorOpt = donorRepository.findById(id);
        if (donorOpt.isPresent()) {
            donationRepository.deleteByDonorId(id);
            donorRepository.deleteById(id);
            return ResponseEntity.ok("Donor and related donations deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Donor not found");
        }
    }

    // ✅ Submit Blood Donation
    @PostMapping("/donate")
    public ResponseEntity<String> donateBlood(@RequestParam Long donorId, @RequestBody Donation donation) {
        Optional<Donor> donorOpt = donorRepository.findById(donorId);
        if (donorOpt.isPresent()) {
            donation.setDonor(donorOpt.get());
            donation.setStatus("Pending");
            donation.setDate(LocalDate.now());
            donationRepository.save(donation);
            return ResponseEntity.ok("Donation submitted successfully");
        } else {
            return ResponseEntity.status(404).body("Donor not found");
        }
    }

    @GetMapping("/{donorId}/donations")
    public ResponseEntity<List<Donation>> getDonationsByDonorId(@PathVariable Long donorId) {
        List<Donation> donations = donationRepository.findByDonorId(donorId);
        return ResponseEntity.ok(donations);
    }

}
