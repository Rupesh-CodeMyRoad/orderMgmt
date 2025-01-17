package com.rupjava.ordermgmt.Dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class UserRegistrationRequest {
    private String name;
    private String email;
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private MultipartFile profilePic; // For profile picture
    private MultipartFile cv;         // For CV
}
