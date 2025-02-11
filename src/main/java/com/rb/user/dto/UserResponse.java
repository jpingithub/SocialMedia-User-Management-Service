package com.rb.user.dto;

import lombok.Data;

@Data
public class UserResponse {

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;

}
