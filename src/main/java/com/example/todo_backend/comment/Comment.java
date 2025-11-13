package com.example.todo_backend.comment;

import java.time.LocalDateTime;

public record Comment(
        String id,
        String commenter,
        String content,
        LocalDateTime date
) {
}
