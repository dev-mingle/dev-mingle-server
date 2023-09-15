package com.example.dm.repository;

import com.example.dm.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsJpaRepository extends JpaRepository<Posts, Long> {
}
