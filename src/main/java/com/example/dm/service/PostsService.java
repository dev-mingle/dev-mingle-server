package com.example.dm.service;

import com.example.dm.dto.posts.PostsAndImages;
import com.example.dm.entity.Images;
import com.example.dm.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostsService {

    Page<Posts> findAll(Long categoryId, String search, List<String> conditions, double latitude, double longitude, Pageable pageable);

    PostsAndImages findById(Long postsId);

    Long update(Long postsId, Posts posts, List<Images> images);

    Long save(Posts posts, List<String> imagesUrlList);

    Long delete(Long postsId);
}
