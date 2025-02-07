package com.rb.user.service;

import com.rb.user.dto.UserRequest;
import com.rb.user.dto.UserResponse;
import com.rb.user.entity.User;

public interface UserService {
    UserResponse saveUser(UserRequest request);
    User findUserById(String id);
    UserResponse updateUser(String userId, UserRequest userRequest);
    UserResponse searchUserByUsername(String userName);
}
