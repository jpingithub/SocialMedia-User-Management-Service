package com.rb.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rb.user.dto.UserRequest;
import com.rb.user.dto.UserResponse;
import com.rb.user.entity.User;
import com.rb.user.exception.UserException;
import com.rb.user.repo.UserRepository;
import com.rb.user.util.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserResponse saveUser(UserRequest request) {
        final User user = objectMapper.convertValue(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if(isUserAvailable(request.getUsername())){
            final User savedUser = userRepository.save(user);
            log.info("User name is unique, data saved successfully");
            return Utilities.convertToUserResponse(savedUser);
        }else {
            log.info("User name already exist : {}",request.getUsername());
            throw new UserException("Username already exist.");
        }
    }

    @Override
    public User findUserById(String id) {
        final Optional<User> optionalUserFromDB = userRepository.findById(id);
        if(optionalUserFromDB.isPresent()) {
            log.info("User found with the id : {}",id);
            return optionalUserFromDB.get();
        } else {
            log.info("No user found with the id : {}",id);
            throw new UserException("User not found with the id : "+id);
        }
    }

    @Override
    public UserResponse updateUser(String userId, UserRequest userRequest) {
        final User userById = findUserById(userId);
        if(userRequest.getFirstName()!=null && !userRequest.getFirstName().equals(userById.getFirstName()))
            userById.setFirstName(userRequest.getFirstName());
        if(userRequest.getLastName()!=null && !userRequest.getLastName().equals(userById.getLastName()))
            userById.setLastName(userRequest.getLastName());
        if(userRequest.getUsername()!=null) {
            log.info("checking new username availability : {}",userRequest.getUsername());
            if(!userRequest.getUsername().equals(userById.getUsername())){
                if(isUserAvailable(userRequest.getUsername())){
                    log.info("Username available to update : {}",userRequest.getUsername());
                    userById.setUsername(userRequest.getUsername());
                }else {
                    log.info("User name not available to update");
                    throw new UserException("Username already exist, and cannot update it.");
                }
            } else log.info("Existed username is same as you provided now : {}",userRequest.getUsername());
        }
        if(userRequest.getPassword()!=null) userById.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return Utilities.convertToUserResponse(userRepository.save(userById));
    }

    @Override
    public User searchUserByUsername(String userName) {
        final Optional<User> optionUser = userRepository.findByUsername(userName);
        if(optionUser.isPresent()){
            log.info("User found with the username : {}",userName);
            return optionUser.get();
        } else {
            log.info("User not found with the username : {}",userName);
            throw new UserException("User not found with the username : "+userName);
        }
    }

    private Boolean isUserAvailable(String username){
        return userRepository.findByUsername(username).isEmpty();
    }

}
