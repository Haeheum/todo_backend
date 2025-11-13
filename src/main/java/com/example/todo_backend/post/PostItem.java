package com.example.todo_backend.post;

import java.time.LocalDateTime;

public record PostItem(
        String id,
        String title,
        String poster,
        String content,
        PostStatus status,
        int itemOrder,
        LocalDateTime date
) {}
