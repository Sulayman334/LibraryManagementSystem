package com.example.LibraryManagementSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NullBookDetails extends RuntimeException{
    public NullBookDetails(String message) {
        super(message);
    }
}
