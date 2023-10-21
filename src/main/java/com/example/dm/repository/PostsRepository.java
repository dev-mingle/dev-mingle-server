package com.example.dm.repository;

import com.example.dm.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostsRepository {

    Page<Posts> findAll(Long categoryId, String search, List<String> conditions, double latitude, double longitude, double distance, Pageable pageable);

    Posts getPosts(Long postsId);

    Posts getPostsWithOptimisticLock(Long postsId);

    Long save(Posts posts);
}
