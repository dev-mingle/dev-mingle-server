package com.example.dm.post.service;

import com.example.dm.post.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostsService {

    Page<Posts> findAll(String search, String[] conditions, String[] location, Pageable pageable);
}
