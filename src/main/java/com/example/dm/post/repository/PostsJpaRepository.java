package com.example.dm.post.repository;

import com.example.dm.post.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsJpaRepository extends JpaRepository<Posts, Long> {
}
