package com.example.todo_backend.post;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepository {
    private final Map<PostStatus, ConcurrentHashMap<String, PostItem>> storageByStatus = Map.of(
            PostStatus.todo, new ConcurrentHashMap<>(),
            PostStatus.onProgress, new ConcurrentHashMap<>(),
            PostStatus.done, new ConcurrentHashMap<>()
    );

    private final AtomicLong idGenerator = new AtomicLong(0);

    private static final Comparator<PostItem> ORDER_COMPARATOR =
            Comparator.comparing(PostItem::itemOrder)
                    .thenComparing(PostItem::date);

    public void save(PostItem item) {
        ConcurrentHashMap<String, PostItem> currentStorage = storageByStatus.get(item.status());

        if (currentStorage == null) {
            throw new IllegalArgumentException("Invalid post status");
        }

        if (item.id() == null || item.id().isEmpty()) {
            String id = String.valueOf(idGenerator.incrementAndGet());
            item = new PostItem(id, item.title(), item.poster(), item.content(), item.status(), item.itemOrder(), item.date());
        }

        for (ConcurrentHashMap<String, PostItem> storage : storageByStatus.values()) {
            storage.remove(item.id());
        }

        currentStorage.put(item.id(), item);
    }

    public Optional<PostItem> findByStatusAndId(PostStatus status, String id) {
        ConcurrentHashMap<String, PostItem> currentStorage = storageByStatus.get(status);

        if (currentStorage == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(currentStorage.get(id));
    }

    public List<PostItem> findByStatus(PostStatus status) {
        ConcurrentHashMap<String, PostItem> currentStorage = storageByStatus.get(status);
        if (currentStorage == null) return Collections.emptyList();

        return currentStorage.values().stream()
                .sorted(ORDER_COMPARATOR)
                .collect(Collectors.toList());
    }

    public int findMaxItemOrder(PostStatus status) {
        ConcurrentHashMap<String, PostItem> currentStorage = storageByStatus.get(status);
        if (currentStorage == null) return 0;

        return currentStorage.values().stream()
                .mapToInt(PostItem::itemOrder)
                .max()
                .orElse(-1) + 1;
    }

    public List<PostItem> findByStatusAndOrderGreaterThan(PostStatus status, int order) {
        ConcurrentHashMap<String, PostItem> currentStorage = storageByStatus.get(status);
        if (currentStorage == null) return Collections.emptyList();

        return currentStorage.values().stream()
                .filter(item -> item.itemOrder() > order)
                .sorted(ORDER_COMPARATOR)
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        storageByStatus.values().forEach(storage -> storage.remove(id));
    }
}
