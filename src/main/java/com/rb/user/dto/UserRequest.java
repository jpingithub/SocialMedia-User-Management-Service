package com.rb.user.dto;

import lombok.Data;

@Data
public class UserRequest {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private Boolean isEmailVerified;

}
