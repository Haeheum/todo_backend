package com.example.todo_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // code 409
public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String username) {
        super("User already exists: " + username);
    }
}
