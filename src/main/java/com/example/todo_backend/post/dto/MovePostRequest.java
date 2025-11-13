package com.example.todo_backend.post.dto;

import com.example.todo_backend.post.PostStatus;

public record MovePostRequest(PostStatus moveFrom, PostStatus moveTo, int newIndex) {
}
