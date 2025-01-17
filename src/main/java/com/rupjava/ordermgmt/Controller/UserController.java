package com.rupjava.ordermgmt.Controller;

import com.rupjava.ordermgmt.Entity.Customer;
import com.rupjava.ordermgmt.Entity.User;
import com.rupjava.ordermgmt.Service.CustomerService;
import com.rupjava.ordermgmt.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {



    @GetMapping("/getDetail")
    public ResponseEntity<Map<String, Object>> getUserDetails(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("name", user.getName());
            userDetails.put("profilePicUrl", user.getProfilePicUrl());
            return ResponseEntity.ok(userDetails);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
