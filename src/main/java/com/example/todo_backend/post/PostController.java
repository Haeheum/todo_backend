package com.example.todo_backend.post;

import com.example.todo_backend.post.dto.AddOrUpdatePostRequest;
import com.example.todo_backend.post.dto.MovePostRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{status}")
    public ResponseEntity<List<PostItem>> getListByStatus(@PathVariable PostStatus status) {
        List<PostItem> items = postService.findByStatus(status);

        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    @PostMapping("/{status}")
    public ResponseEntity<Void> addItem(
            @PathVariable PostStatus status,
            @RequestBody AddOrUpdatePostRequest request) {
        postService.saveItem(status, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{status}/{id}")
    public ResponseEntity<Void> updateItem(
            @PathVariable PostStatus status,
            @PathVariable String id,
            @RequestBody AddOrUpdatePostRequest request) {
        postService.updateItem(status, id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{status}/{id}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable PostStatus status,
            @PathVariable String id) {
        postService.deleteItem(status, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/move/{id}")
    public ResponseEntity<Void> moveItem(
            @PathVariable String id,
            @RequestBody MovePostRequest moveRequest) {
        postService.moveItem(id, moveRequest);
        return ResponseEntity.ok().build();
    }
}
