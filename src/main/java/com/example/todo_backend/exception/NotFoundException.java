package com.example.todo_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // code 404
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
