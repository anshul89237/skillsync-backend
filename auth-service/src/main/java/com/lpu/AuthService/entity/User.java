package com.lpu.AuthService.entity;

import java.util.ArrayList;
import java.util.List;

import com.lpu.java.common_security.audit.BaseEntity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
	    name = "users",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = "email")
	    },
	    indexes = {
	        @Index(name = "idx_user_email", columnList = "email"),
	        @Index(name = "idx_user_role", columnList = "role")
	    }
	)
@Getter
@Setter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Column(length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false, length = 255)
    private String password;

    @NotBlank(message = "Role is required")
    @Column(nullable = false, length = 20)
    private String role;

    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[A-Za-z]{2,50}$",
             message = "First name must contain only letters (2-50)")
    @Column(nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[A-Za-z]{2,50}$",
             message = "Last name must contain only letters (2-50)")
    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(length = 120)
    private String headline;

    @Column(length = 150)
    private String learningGoal;

    @Column
    private Boolean emailNotifications = true;

    @Column
    private Boolean sessionReminders = true;

    @Column
    private Boolean marketingUpdates = false;

    @Column(length = 20)
    private String profileVisibility = "learners";

    @Column
    private Boolean twoFactorEnabled = false;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private boolean isVerified = false;
    
    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "user_providers", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "provider")
    private List<String> providers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_skills", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "skill_id")
    private List<Long> skillIds = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void normalize() {
        if (email != null) {
            email = email.toLowerCase();
        }
        if (emailNotifications == null) {
            emailNotifications = true;
        }
        if (sessionReminders == null) {
            sessionReminders = true;
        }
        if (marketingUpdates == null) {
            marketingUpdates = false;
        }
        if (profileVisibility == null || profileVisibility.isBlank()) {
            profileVisibility = "learners";
        }
        if (twoFactorEnabled == null) {
            twoFactorEnabled = false;
        }
    }
}