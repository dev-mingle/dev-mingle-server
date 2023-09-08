package com.example.dm.post.repository;

import com.example.dm.post.entity.Posts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostsRepositoryImpl implements PostsRepository{

    private final PostsJpaRepository postsJpaRepository;

    @Override
    public Page<Posts> findAll(Long categoryId, String search, String[] conditions, String[] location, Pageable pageable) {
        return null;
    }
}
