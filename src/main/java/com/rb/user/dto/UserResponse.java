package com.rb.user.dto;

import lombok.Data;

@Data
public class UserResponse {

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String profileImageUrl;
    private String email;

}
