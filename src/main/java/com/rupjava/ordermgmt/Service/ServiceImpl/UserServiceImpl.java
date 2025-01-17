package com.rupjava.ordermgmt.Service.ServiceImpl;

import com.rupjava.ordermgmt.Dto.UserRegistrationRequest;
import com.rupjava.ordermgmt.Entity.Otp;
import com.rupjava.ordermgmt.Entity.User;
import com.rupjava.ordermgmt.Repository.OtpRepository;
import com.rupjava.ordermgmt.Repository.UserRepository;
import com.rupjava.ordermgmt.Service.UserService;
import com.rupjava.ordermgmt.Utils.EmailService;
import com.rupjava.ordermgmt.Utils.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileHandler fileHandler;

    public void registerUser(UserRegistrationRequest userRequest) throws IOException {
        // Map DTO to User entity
        User user = UserRequestToUserDto(userRequest);

        // Save uploaded profile picture
        String profilePicUrl = fileHandler.saveFile(userRequest.getProfilePic(), "profile_pics/");
        user.setProfilePicUrl(profilePicUrl);

        // Save uploaded CV
        String cvUrl = fileHandler.saveFile(userRequest.getCv(), "cv/");
        user.setCvUrl(cvUrl);

        // Encrypt password and set default values
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setVerified(false);

        // Save user
        userRepository.save(user);

        // Generate OTP
        String otpCode = String.valueOf((int) (Math.random() * 9000) + 1000);
        Otp otp = new Otp();
        otp.setEmail(user.getEmail());
        otp.setOtpCode(otpCode);
        otp.setCreatedAt(LocalDateTime.now());
        otpRepository.save(otp);

        // Send OTP email
        emailService.sendOtpEmail(user.getEmail(), otpCode);
    }


    public boolean verifyOtp(String email, String otpCode) {
        Optional<Otp> otpOptional = otpRepository.findByEmailAndOtpCodeAndUsedFalse(email, otpCode);
        if (otpOptional.isPresent()) {
            Otp otp = otpOptional.get();
            if (otp.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(5))) {
                otp.setUsed(true);
                otpRepository.save(otp);

                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
                user.setVerified(true);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    public User UserRequestToUserDto(UserRegistrationRequest userRegistrationRequest) {
        User user = new User();
        user.setName(userRegistrationRequest.getName());
        user.setEmail(userRegistrationRequest.getEmail());
        user.setPassword(userRegistrationRequest.getPassword());
        user.setDateOfBirth(userRegistrationRequest.getDateOfBirth());
        return user;
    }
}
