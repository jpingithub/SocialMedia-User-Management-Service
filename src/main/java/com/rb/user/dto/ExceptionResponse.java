package com.rb.user.dto;

import lombok.Data;

@Data
public class ExceptionResponse {

    private String message;
    private String path;
    private String timeStamp;
    private int status;
}
