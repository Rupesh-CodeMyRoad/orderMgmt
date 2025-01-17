package com.rupjava.ordermgmt.Controller;

import com.rupjava.ordermgmt.Dto.UserRegistrationRequest;
import com.rupjava.ordermgmt.Entity.User;
import com.rupjava.ordermgmt.Repository.UserRepository;
import com.rupjava.ordermgmt.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> createUser(@ModelAttribute UserRegistrationRequest userRequest) throws IOException {
        userService.registerUser(userRequest);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }


    @GetMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        if (userService.verifyOtp(email, otp)) {
            return ResponseEntity.ok(Map.of("message", "OTP verified successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "OTP verification failed"));
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody UserRegistrationRequest loginRequestData,
            HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestData.getEmail(),
                            loginRequestData.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Fetch user details and roles
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Store user and roles in session
            session.setAttribute("user", user);
            session.setAttribute("roles", roles);

            // Debugging: Log session attributes
            Enumeration<String> attributeNames = session.getAttributeNames();
            System.out.println("Session Attributes:");
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                System.out.println(attributeName + ": " + session.getAttribute(attributeName));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Login Success",
                    "roles", roles
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }
    }



    @GetMapping("/validate-session")
    public ResponseEntity<Boolean> validateSession(HttpSession session) {
        Object user = session.getAttribute("user"); // Check if user is stored in session
        if (user != null) {
            return ResponseEntity.ok(true); // Session is valid
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false); // No valid session
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return ResponseEntity.ok(Map.of("message", "Logout Success"));
    }

}
