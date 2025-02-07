package com.rb.user.controller;

import com.rb.user.dto.UserRequest;
import com.rb.user.dto.UserResponse;
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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") String id){
        return new ResponseEntity<>(Utilities.convertToUserResponse(userService.findUserById(id)), HttpStatus.FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") String id ,@RequestBody UserRequest userRequest){
        return new ResponseEntity<>(userService.updateUser(id,userRequest),HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<UserResponse> searchUser(@RequestParam("username") String username){
        return new ResponseEntity<>(userService.searchUserByUsername(username),HttpStatus.FOUND);
    }

}
