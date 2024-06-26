package com.berkan.bachelorprojekt.BA_Bachelorprojekt.Exceptions;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserException extends RuntimeException {
    public UserException() {
    }
    public UserException(String message) {
        super(message);
    }
}
