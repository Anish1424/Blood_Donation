package com.project.bloodbridge.service;

import com.project.bloodbridge.model.Admin;
import com.project.bloodbridge.model.Donor;
import com.project.bloodbridge.model.Donation;

import java.util.List;
import java.util.Map;

public interface AdminService {
    Admin registerAdmin(Admin admin);
    Admin loginAdmin(String email, String password);
    void generateAndSendOtp(String email);
    void resetPasswordWithOtp(String email, String otp, String newPassword);
    Map<String, Long> getDashboardStats();
    Map<String, Integer> getBloodStockCounts();
    List<Donor> getAllDonors();
    List<Donation> getAllDonations();

    // ✅ NEW
    void approveDonation(Long donationId);
    void rejectDonation(Long donationId);
}
