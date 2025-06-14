package com.project.bloodbridge.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "otp")
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String otp;
    private Date createdAt;

    public OtpEntity() {}

    public OtpEntity(String email, String otp, Date createdAt) {
        this.email = email;
        this.otp = otp;
        this.createdAt = createdAt;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

    // Getters and Setters
    
}
