package com.rupjava.ordermgmt.Service;

import com.rupjava.ordermgmt.Dto.UserRegistrationRequest;
import com.rupjava.ordermgmt.Entity.User;

import java.io.IOException;
import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    void registerUser(UserRegistrationRequest userRequest) throws IOException;


    boolean verifyOtp(String email, String otp);

    void deleteUser(Long id);
}
