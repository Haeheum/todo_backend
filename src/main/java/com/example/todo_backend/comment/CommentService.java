package com.example.todo_backend.comment;

import com.example.todo_backend.comment.dto.AddCommentRequest;
import com.example.todo_backend.comment.dto.PaginatedCommentsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void addComment(AddCommentRequest request) {
        Comment newComment = new Comment(
                "",
                request.commenter(),
                request.content(),
                request.date()
        );
        commentRepository.save(newComment);
    }

    public PaginatedCommentsResponse getComments(int page, int size) {
        List<Comment> allComments = commentRepository.findAll();

        int totalElements = allComments.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        int start = page * size;
        int end = Math.min(start + size, totalElements);

        List<Comment> pageComments = (start < totalElements)
                ? allComments.subList(start, end)
                : List.of();

        return new PaginatedCommentsResponse(
                pageComments,
                totalPages,
                totalElements,
                page
        );
    }
}