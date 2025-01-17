package com.rupjava.ordermgmt.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Full name of the user

    @Column(nullable = false, unique = true)
    private String email; // Email address (used as username)

    private String profilePicUrl; // URL of the uploaded profile picture

    private String cvUrl; // URL of the uploaded CV

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth; // User's date of birth

    @Column(nullable = false)
    private String password; // Encrypted password

    @Column(nullable = false)
    private String role; // ROLE_USER or ROLE_ADMIN

    private boolean verified = false; // OTP verification status

}
