package com.berkan.bachelorprojekt.BA_Bachelorprojekt.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {UserException.class})
    protected ResponseEntity<String> handleUser(RuntimeException ex, WebRequest request){
        String bodyOfResponse = "User Error: " + ex.getMessage();

        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = {XRToolError.class})
    protected ResponseEntity<String> handleXRTool(RuntimeException ex, WebRequest request){
        String bodyOfResponse = "XRTool Error: " + ex.getMessage();


        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {SearchError.class})
    protected ResponseEntity<String> handleSearch(RuntimeException ex, WebRequest request){
        String bodyOfResponse = "Search Error: " + ex.getMessage();


        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }


}