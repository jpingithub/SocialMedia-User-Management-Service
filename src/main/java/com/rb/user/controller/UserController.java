package com.rb.user.controller;

import com.rb.user.dto.UserRequest;
import com.rb.user.dto.UserResponse;
import com.rb.user.entity.OTP;
import com.rb.user.entity.User;
import com.rb.user.service.UserService;
import com.rb.user.util.Utilities;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> saveUser(@RequestBody UserRequest userRequest){
        return new ResponseEntity<>(userService.saveUser(userRequest), HttpStatus.CREATED);
    }

    @GetMapping("/otp")
    public ResponseEntity<OTP> sendOtp(@RequestParam("email")String toEmail){
        return new ResponseEntity<>(userService.generateOTP(toEmail),HttpStatus.OK);
    }

    @PostMapping("/validate/otp")
    public ResponseEntity<Boolean> verifyOtp(@RequestParam("email")String email,@RequestParam("otp")Integer otp){
        return new ResponseEntity<>(userService.validateOtp(email,otp),HttpStatus.OK);
    }

    @GetMapping("current/details")
    public ResponseEntity<UserResponse> getUserById(@RequestHeader("${customized-header-for-token}") String loggedInUsername){
        return new ResponseEntity<>(Utilities.convertToUserResponse(userService.findByUserName(loggedInUsername)), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestHeader("${customized-header-for-token}") String loggedInUsername ,@RequestBody UserRequest userRequest){
        return new ResponseEntity<>(userService.updateUser(loggedInUsername,userRequest),HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<UserResponse> searchUser(@RequestParam("username") String username){
        return new ResponseEntity<>(Utilities.convertToUserResponse(userService.searchUserByUsername(username)),HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<User> getUserByUsernameForLogin(@RequestParam("username") String username){
        return new ResponseEntity<>(userService.searchUserByUsername(username),HttpStatus.OK);
    }

}
