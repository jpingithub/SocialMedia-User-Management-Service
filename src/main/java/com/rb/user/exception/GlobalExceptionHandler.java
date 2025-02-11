package com.rb.user.exception;

import com.rb.user.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ExceptionResponse> handleUserException(UserException ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setPath(request.getDescription(false));
        exceptionResponse.setTimeStamp(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoUserFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoUserFoundException(NoUserFoundException ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setPath(request.getDescription(false));
        exceptionResponse.setTimeStamp(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NO_CONTENT);
    }

}
