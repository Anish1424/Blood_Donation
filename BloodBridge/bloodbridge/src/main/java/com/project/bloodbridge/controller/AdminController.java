package com.project.bloodbridge.controller;

import com.project.bloodbridge.model.Admin;
import com.project.bloodbridge.model.BloodRequest;
import com.project.bloodbridge.model.BloodStock;
import com.project.bloodbridge.model.Donation;
import com.project.bloodbridge.model.Donor;
import com.project.bloodbridge.repository.AdminRepository;
import com.project.bloodbridge.repository.BloodRequestRepository;
import com.project.bloodbridge.repository.DonationRepository;
import com.project.bloodbridge.repository.DonorRepository;
import com.project.bloodbridge.repository.StockRepository;
import com.project.bloodbridge.service.EmailService;
import com.project.bloodbridge.service.OtpService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DonationRepository donationRepository;
    
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //  Admin Registration
    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestBody Admin admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already registered");
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
        return ResponseEntity.ok("Admin registered successfully");
    }

    //  Admin Login
    @PostMapping("/login")
    public ResponseEntity<String> loginAdmin(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (passwordEncoder.matches(password, admin.getPassword())) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(401).body("Invalid password");
            }
        } else {
            return ResponseEntity.status(404).body("Admin not found");
        }
    }

    //  Send OTP
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Email not registered");
        }

        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);
        return ResponseEntity.ok("OTP sent to your registered email");
    }

    //  Verify OTP
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
            return ResponseEntity.status(400).body("Invalid or expired OTP");
        }
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        if (email == null || newPassword == null || email.isEmpty() || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("Email and new password are required");
        }

        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            admin.setPassword(passwordEncoder.encode(newPassword));
            adminRepository.save(admin);
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.status(404).body("Admin not found");
        }
    }

    //  Dashboard Stats
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        long totalDonors = donorRepository.count();
        long totalRequests = donationRepository.count();  // total donation requests
        long approvedRequests = donationRepository.countByStatus("Approved");
        
        // Sum total blood units donated (only approved)
        Long totalBloodUnits = donationRepository.sumUnitsByStatus("Approved");
        if (totalBloodUnits == null) totalBloodUnits = 0L;

        Map<String, Long> stats = new HashMap<>();
        stats.put("totalDonors", totalDonors);
        stats.put("totalRequests", totalRequests);
        stats.put("approvedRequests", approvedRequests);
        stats.put("totalBloodUnits", totalBloodUnits);

        return ResponseEntity.ok(stats);
    }


    //  Dashboard Blood Stock (by group)
    @GetMapping("/dashboard/bloodstock")
    public ResponseEntity<Map<String, Integer>> getBloodStock() {
        List<BloodStock> stocks = stockRepository.findAll();

        Map<String, Integer> stockMap = new HashMap<>();
        // Initialize groups with 0 if missing
        List<String> groups = Arrays.asList("A+", "B+", "O+", "AB+", "A-", "B-", "O-", "AB-");
        for (String group : groups) {
            stockMap.put(group, 0);
        }

        for (BloodStock stock : stocks) {
            stockMap.put(stock.getBloodGroup(), stock.getUnit());
        }

        return ResponseEntity.ok(stockMap);
    }
    
    @GetMapping("/blood-stock")
    public List<BloodStock> getAllStock() {
        return stockRepository.findAll();
    }
    
    
    @PostMapping("/api/admin/blood-stock/update")
    public ResponseEntity<String> updateStock(@RequestParam String bloodGroup, @RequestParam int units) {
        String group = bloodGroup.trim().toUpperCase();  // normalize
        Optional<BloodStock> stockOpt = stockRepository.findByBloodGroup(group);
        if (stockOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blood group not found");
        }
        BloodStock stock = stockOpt.get();
        stock.setUnit(stock.getUnit() + units);
        stockRepository.save(stock);
        return ResponseEntity.ok("Stock updated successfully");
    }


    //  All Donors (Admin View)
    @GetMapping("/donors")
    public ResponseEntity<List<Donor>> getAllDonors() {
        return ResponseEntity.ok(donorRepository.findAll());
    }

    //  All Donations (Admin View)
    @GetMapping("/donations")
    public ResponseEntity<List<Donation>> getAllDonations() {
        return ResponseEntity.ok(donationRepository.findAll());
    }

    // Approve Donation
    @PostMapping("/blood-requests/{id}/approve")
    public ResponseEntity<String> approveBloodRequest(@PathVariable Long id) {
        Optional<BloodRequest> opt = bloodRequestRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.status(404).body("Request not found");

        BloodRequest request = opt.get();
        String group = request.getBloodGroup();
        int requestedUnit = request.getUnit();

        Optional<BloodStock> stockOpt = stockRepository.findByBloodGroup(group);
        int available = stockOpt.map(BloodStock::getUnit).orElse(0);

        if (requestedUnit > available) {
            return ResponseEntity.status(400).body("Not enough blood units available");
        }

        // Deduct unit from stock
        BloodStock stock = stockOpt.get();
        stock.setUnit(available - requestedUnit);
        stockRepository.save(stock);

        request.setStatus("Approved");
        bloodRequestRepository.save(request);
        return ResponseEntity.ok("Request approved");
    }
    
 //  Approve Donation and Update BloodStock
    @PostMapping("/donations/{id}/approve")
    public ResponseEntity<String> approveDonation(@PathVariable Long id) {
        Optional<Donation> opt = donationRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.status(404).body("Donation not found");

        Donation donation = opt.get();

        // Only approve if not already approved
        if (!"Approved".equalsIgnoreCase(donation.getStatus())) {
            donation.setStatus("Approved");
            donationRepository.save(donation);

            // Update blood stock
            updateBloodStock(donation.getBloodGroup(), donation.getUnit());
        }

        return ResponseEntity.ok("Donation approved and stock updated");
    }



    // Reject Donation remains same
    @PostMapping("/donations/{id}/reject")
    public ResponseEntity<String> rejectDonation(@PathVariable Long id) {
        return donationRepository.findById(id).map(donation -> {
            donation.setStatus("Rejected");
            donationRepository.save(donation);
            return ResponseEntity.ok("Donation rejected");
        }).orElse(ResponseEntity.status(404).body("Donation not found"));
    }



    // Method to update BloodStock repository
    private void updateBloodStock(String bloodGroup, int unitsToAdd) {
        Optional<BloodStock> stockOpt = stockRepository.findByBloodGroup(bloodGroup);
        BloodStock stock;
        if (stockOpt.isPresent()) {
            stock = stockOpt.get();
            stock.setUnit(stock.getUnit() + unitsToAdd);
        } else {
            // Create new stock entry if not exists
            stock = new BloodStock();
            stock.setBloodGroup(bloodGroup);
            stock.setUnit(unitsToAdd);
        }
        stockRepository.save(stock);
    }

}