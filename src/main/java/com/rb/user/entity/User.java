package com.rb.user.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    @Column(length = 100000)
    private String profileImageUrl;
    private Boolean isEmailVerified;
}
