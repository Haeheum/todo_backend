package com.example.todo_backend.post.dto;

import java.time.LocalDateTime;

public record AddOrUpdatePostRequest(
        String id,
        String title,
        String poster,
        String content,
        LocalDateTime date
) {
}
