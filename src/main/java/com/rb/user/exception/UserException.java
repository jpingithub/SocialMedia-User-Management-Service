package com.rb.user.exception;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException{
    public UserException(String message){
        super(message);
    }
}
