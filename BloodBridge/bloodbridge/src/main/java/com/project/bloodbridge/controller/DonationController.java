package com.project.bloodbridge.controller;

import com.project.bloodbridge.model.Donation;
import com.project.bloodbridge.model.Donor;
import com.project.bloodbridge.repository.DonationRepository;
import com.project.bloodbridge.repository.DonorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

@RestController
public class DonationController {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonorRepository donorRepository;

    private final String UPLOAD_DIR = "C:/bloodbridge/uploads/";


    // ✅ Donor: Submit Donation
    @PostMapping("/api/donations/by-donor")
    public ResponseEntity<String> uploadDonation(
            @RequestParam("donorId") Long donorId,
            @RequestParam("bloodGroup") String bloodGroup,
            @RequestParam("unit") Integer unit,
            @RequestParam("disease") String disease,
            @RequestParam("age") Integer age,
            @RequestParam("prescription") MultipartFile file
    ) {
        try {
            Optional<Donor> donorOpt = donorRepository.findById(donorId);
            if (donorOpt.isEmpty()) return ResponseEntity.badRequest().body("Donor not found");

            if (file.isEmpty()) return ResponseEntity.badRequest().body("Prescription is required");

            // Save file
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File uploadPath = new File(UPLOAD_DIR);
            if (!uploadPath.exists()) uploadPath.mkdirs();

            File dest = new File(uploadPath, fileName);
            file.transferTo(dest);

            // Create donation entry
            Donation donation = new Donation();
            donation.setDonor(donorOpt.get());
            donation.setBloodGroup(bloodGroup);
            donation.setUnit(unit);
            donation.setDisease(disease);
            donation.setAge(age);
            donation.setDate(LocalDate.now());
            donation.setPrescriptionPath(fileName); // ✅ Just the name
            donation.setStatus("Pending");

            donationRepository.save(donation);
            return ResponseEntity.ok("Donation submitted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    // ✅ Donor: Donation History
    @GetMapping("/donation-history")
    public List<Map<String, Object>> getDonationHistory(@RequestParam String email) {
        Optional<Donor> donorOpt = donorRepository.findByEmail(email);
        if (donorOpt.isEmpty()) return Collections.emptyList();

        List<Donation> donations = donorOpt.get().getDonations();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Donation d : donations) {
            Map<String, Object> data = new HashMap<>();
            data.put("age", d.getAge());
            data.put("disease", d.getDisease());
            data.put("bloodGroup", d.getBloodGroup());
            data.put("unit", d.getUnit());
            data.put("date", d.getDate());
            data.put("status", d.getStatus());
            result.add(data);
        }

        return result;
    }

    // ✅ Admin: All Donations
    @GetMapping("/api/donations")
    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }
}
