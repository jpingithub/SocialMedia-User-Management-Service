package com.rb.user.service;

import com.rb.user.entity.OTP;
import com.rb.user.dto.UserRequest;
import com.rb.user.dto.UserResponse;
import com.rb.user.entity.User;

public interface UserService {
    UserResponse saveUser(UserRequest request);
    User findByUserName(String id);
    UserResponse updateUser(String userName, UserRequest userRequest);
    User searchUserByUsername(String userName);
    OTP generateOTP(String email);
    Boolean validateOtp(String belongsTo,Integer otp);
}
