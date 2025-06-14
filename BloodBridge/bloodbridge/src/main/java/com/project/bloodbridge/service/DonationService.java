package com.project.bloodbridge.service;

import com.project.bloodbridge.model.Donation;
import com.project.bloodbridge.model.Donor;
import com.project.bloodbridge.repository.DonationRepository;
import com.project.bloodbridge.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonorRepository donorRepository;

    //  Save donation with donor linkage
    public Donation saveDonation(String email, String bloodGroup, int unit, String disease, int age, MultipartFile prescription) throws IOException {
        // Find donor by email
        Optional<Donor> donorOptional = donorRepository.findByEmail(email);
        if (donorOptional.isEmpty()) {
            throw new RuntimeException("Donor not found with email: " + email);
        }

        // Upload prescription
        String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(prescription.getOriginalFilename());
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) uploadFolder.mkdirs();

        File saveFile = new File(uploadFolder, fileName);
        prescription.transferTo(saveFile);

        // Create and save donation
        Donation donation = new Donation();
        donation.setDonor(donorOptional.get());
        donation.setBloodGroup(bloodGroup);
        donation.setUnit(unit);
        donation.setDisease(disease);
        donation.setAge(age);
        donation.setPrescriptionPath(Paths.get("uploads", fileName).toString());
        donation.setDate(LocalDate.now());
        donation.setStatus("Pending");

        return donationRepository.save(donation);
    }

    //  Get donation history by email
    public List<Donation> getDonationHistory(String email) {
        return donationRepository.findByDonorEmail(email);
    }

    //Get all donations
    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    //  Get one donation by ID
    public Optional<Donation> getDonationById(Long id) {
        return donationRepository.findById(id);
    }

    //  Update donation
    public Donation updateDonation(Long id, Donation updated) {
        return donationRepository.findById(id).map(existing -> {
            existing.setBloodGroup(updated.getBloodGroup());
            existing.setUnit(updated.getUnit());
            existing.setAge(updated.getAge());
            existing.setDisease(updated.getDisease());
            existing.setPrescriptionPath(updated.getPrescriptionPath());
            existing.setStatus(updated.getStatus());
            return donationRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Donation not found with ID: " + id));
    }

    //  Delete donation
    public void deleteDonation(Long id) {
        donationRepository.deleteById(id);
    }
}

