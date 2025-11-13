package com.example.todo_backend.comment.dto;

import com.example.todo_backend.comment.Comment;

import java.util.List;

public record PaginatedCommentsResponse(
        List<Comment> comments,
        int totalPages,
        long totalElements,
        int currentPage
) {}
