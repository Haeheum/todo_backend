package com.example.todo_backend.comment.dto;

import java.time.LocalDateTime;

public record AddCommentRequest(
        String commenter,
        String content,
        LocalDateTime date
) {
}
