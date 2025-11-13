package com.example.todo_backend.auth;

import com.example.todo_backend.exception.DuplicateUsernameException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AuthRepository {
    final ConcurrentHashMap<String, User> userData = new ConcurrentHashMap<>();

    public void save(User user) {
        if (userData.containsKey(user.username())) {
            throw new DuplicateUsernameException(user.username());
        }
        userData.put(user.username(), user);
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userData.get(username));
    }

}
