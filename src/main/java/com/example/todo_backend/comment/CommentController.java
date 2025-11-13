package com.example.todo_backend.comment;

import com.example.todo_backend.comment.dto.AddCommentRequest;
import com.example.todo_backend.comment.dto.PaginatedCommentsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<PaginatedCommentsResponse> getComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedCommentsResponse response = commentService.getComments(page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> addComment(
            @RequestBody AddCommentRequest request) {

        commentService.addComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
