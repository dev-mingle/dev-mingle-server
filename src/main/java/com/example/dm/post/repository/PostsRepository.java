package com.example.dm.post.repository;

import com.example.dm.post.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostsRepository {

    Page<Posts> findAll(Long categoryId, String search, String[] conditions, String[] location, Pageable pageable);
}
