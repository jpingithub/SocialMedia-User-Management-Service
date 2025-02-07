package com.rb.user.util;

import com.rb.user.dto.UserResponse;
import com.rb.user.entity.User;

public class Utilities {
    public static UserResponse convertToUserResponse(User user){
        final UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        return userResponse;
    }
}
