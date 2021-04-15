package com.example.contactlistserver;

import com.example.contactlistserver.exception.ContactNotFoundException;
import com.example.contactlistserver.exception.IllegalRequestException;
import com.example.contactlistserver.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Contact not found")
    @ExceptionHandler(ContactNotFoundException.class)
    public void handleContactNotFoundException() { }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
    @ExceptionHandler(UserNotFoundException.class)
    public void handleUserNotFoundException() { }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Illegal request")
    @ExceptionHandler(IllegalRequestException.class)
    public void handleIllegalRequestException() { }
}
