package com.project.bloodbridge.service;

import com.project.bloodbridge.model.*;
import com.project.bloodbridge.repository.*;
import com.project.bloodbridge.service.AdminService;
import com.project.bloodbridge.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    @Autowired
    private StockRepository bloodStockRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailUtil emailUtil;

    // ------------------------ OLD METHODS -----------------------

    @Override
    public Admin registerAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin loginAdmin(String email, String password) {
        return adminRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }

    @Override
    public void generateAndSendOtp(String email) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        OtpEntity otpEntity = new OtpEntity(email, otp, new Date());
        otpRepository.save(otpEntity);

        emailUtil.sendOtpEmail(email,"Your OTP for BloodBridge Login", otp);
    }

    @Override
    public void resetPasswordWithOtp(String email, String otp, String newPassword) {
        OtpEntity otpEntity = otpRepository.findByEmailAndOtp(email, otp)
                .orElseThrow(() -> new RuntimeException("Invalid or expired OTP"));

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        admin.setPassword(newPassword);
        adminRepository.save(admin);

        otpRepository.delete(otpEntity);
    }

    // ------------------------ NEW METHODS -----------------------

    @Override
    public Map<String, Long> getDashboardStats() {
        long totalDonors = donorRepository.count();
        long totalRequests = bloodRequestRepository.count();
        long approvedRequests = bloodRequestRepository.countByStatus("Approved");
        long totalBloodUnits = donationRepository.findAll().stream()
                .filter(d -> "Approved".equalsIgnoreCase(d.getStatus()))
                .mapToLong(Donation::getUnit)
                .sum();

        Map<String, Long> stats = new HashMap<>();
        stats.put("totalDonors", totalDonors);
        stats.put("totalRequests", totalRequests);
        stats.put("approvedRequests", approvedRequests);
        stats.put("totalBloodUnits", totalBloodUnits);
        return stats;
    }

    @Override
    public Map<String, Integer> getBloodStockCounts() {
        List<BloodStock> stockList = bloodStockRepository.findAll();
        Map<String, Integer> bloodMap = new HashMap<>();
        for (BloodStock stock : stockList) {
            bloodMap.put(stock.getBloodGroup(), stock.getUnit());
        }
        return bloodMap;
    }

    @Override
    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    @Override
    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }
    
    @Override
    public void approveDonation(Long donationId) {
        Donation donation = donationRepository.findById(donationId)
            .orElseThrow(() -> new RuntimeException("Donation not found"));
        donation.setStatus("Approved");
        donationRepository.save(donation);
    }

    @Override
    public void rejectDonation(Long donationId) {
        Donation donation = donationRepository.findById(donationId)
            .orElseThrow(() -> new RuntimeException("Donation not found"));
        donation.setStatus("Rejected");
        donationRepository.save(donation);
    }

}
