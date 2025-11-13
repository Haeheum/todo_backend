package com.example.todo_backend.post;

import com.example.todo_backend.exception.NotFoundException;
import com.example.todo_backend.post.dto.AddOrUpdatePostRequest;
import com.example.todo_backend.post.dto.MovePostRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostItem> findByStatus(PostStatus status) {
        return postRepository.findByStatus(status);
    }

    public void saveItem(PostStatus status, AddOrUpdatePostRequest request) {

        int nextOrder = postRepository.findMaxItemOrder(status);
        PostItem newItem = new PostItem(
                "",
                request.title(),
                request.poster(),
                request.content(),
                status,
                nextOrder,
                request.date()
        );
        postRepository.save(newItem);
    }

    public void updateItem(PostStatus status, String id, AddOrUpdatePostRequest request) {
        PostItem existingItem = postRepository.findByStatusAndId(status, id)
                .orElseThrow(() -> new NotFoundException("Not found"));

        PostItem updatedItem = new PostItem(
                id,
                request.title(),
                request.poster(),
                request.content(),
                existingItem.status(),
                existingItem.itemOrder(),
                request.date()
        );
        postRepository.save(updatedItem);
    }

    public void deleteItem(PostStatus status, String id) {
        PostItem deletedItem = postRepository.findByStatusAndId(status, id)
                .orElseThrow(() -> new NotFoundException("Not found"));

        int deletedOrder = deletedItem.itemOrder();

        postRepository.deleteById(id);

        List<PostItem> itemsToReorder = postRepository.findByStatusAndOrderGreaterThan(status, deletedOrder);

        for (PostItem item : itemsToReorder) {
            PostItem reorderedItem = new PostItem(
                    item.id(), item.title(), item.poster(), item.content(),
                    item.status(), item.itemOrder() - 1, item.date()
            );
            postRepository.save(reorderedItem);
        }
    }

    public void moveItem(String id, MovePostRequest request) {
        PostItem itemToMove = postRepository.findByStatusAndId(request.moveFrom(), id)
                .orElseThrow(() -> new NotFoundException("Not found"));

        List<PostItem> itemsToShiftUp = postRepository.findByStatusAndOrderGreaterThan(request.moveFrom(), itemToMove.itemOrder());
        for (PostItem item : itemsToShiftUp) {
            PostItem shiftedItem = new PostItem(
                    item.id(), item.title(), item.poster(), item.content(),
                    item.status(), item.itemOrder() - 1, item.date()
            );
            postRepository.save(shiftedItem);
        }

        List<PostItem> itemsToShiftDown = postRepository.findByStatusAndOrderGreaterThan(request.moveTo(), request.newIndex() - 1);
        for (PostItem item : itemsToShiftDown) {
            PostItem shiftedItem = new PostItem(
                    item.id(), item.title(), item.poster(), item.content(),
                    item.status(), item.itemOrder() + 1, item.date()
            );
            postRepository.save(shiftedItem);
        }

        PostItem movedItem = new PostItem(
                id, itemToMove.title(), itemToMove.poster(), itemToMove.content(),
                request.moveTo(),
                request.newIndex(),
                itemToMove.date()
        );
        postRepository.save(movedItem);
    }
}
