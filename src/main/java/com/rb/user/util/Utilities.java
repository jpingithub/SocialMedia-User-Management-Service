package com.rb.user.util;

import com.rb.user.dto.UserResponse;
import com.rb.user.entity.User;

import java.util.Random;

public class Utilities {

    private static final Random random = new Random();

    public static UserResponse convertToUserResponse(User user){
        final UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setProfileImageUrl(user.getProfileImageUrl());
        return userResponse;
    }

    public static Integer getOtp(){
        return 1000+random.nextInt(9000);
    }

}
