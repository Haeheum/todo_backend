package com.example.todo_backend.auth;

import com.example.todo_backend.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void registerUser(LoginRequest signUpRequest) {
        User newUser = new User(signUpRequest.username(), signUpRequest.password());
        authRepository.save(newUser);
    }

    public String authenticateUser(LoginRequest loginRequest) {
        User storedUser = authRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new UnauthorizedException("Login fail"));

        if (!storedUser.password().equals(loginRequest.password())) {
            throw new UnauthorizedException("Login fail");
        }

        return storedUser.username();
    }
}
