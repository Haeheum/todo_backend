package com.example.todo_backend.comment;

import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class CommentRepository {
    private final ConcurrentHashMap<String, Comment> storage = new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(0);

    private static final Comparator<Comment> DATE_DESC_COMPARATOR =
            Comparator.comparing(Comment::date).reversed();

    public void save(Comment comment) {
        Comment savedComment = comment;

        if (comment.id() == null || comment.id().isEmpty()) {
            String newId = String.valueOf(idGenerator.incrementAndGet());
            savedComment = new Comment(newId, comment.commenter(), comment.content(), comment.date());
        }

        storage.put(savedComment.id(), savedComment);
    }

    public List<Comment> findAll() {
        return storage.values().stream()
                .sorted(DATE_DESC_COMPARATOR)
                .collect(Collectors.toList());
    }

}
