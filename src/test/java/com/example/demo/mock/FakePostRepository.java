package com.example.demo.mock;

import com.example.demo.post.domain.Post;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakePostRepository implements PostRepository {

    private Long id = 1L;
    private final List<Post> data = new ArrayList<>();

    @Override
    public Optional<Post> findById(long id) {
        return data.stream()
                .filter(p -> p.getId().equals(id))
                .findAny();
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null || post.getId() == 0) {
            Post newPost = Post.builder()
                    .id(this.id++)
                    .content(post.getContent())
                    .writer(post.getWriter())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .build();
            data.add(newPost);
            return newPost;
        } else {
            data.removeIf(u -> u.getId().equals(post.getId()));
            data.add(post);
            return null;
        }
    }
}
