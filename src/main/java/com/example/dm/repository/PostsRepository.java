package com.example.dm.repository;

import com.example.dm.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostsRepository {

    Page<Posts> findAll(Long categoryId, String search, String[] conditions, double[] location, double distance, Pageable pageable);
}
