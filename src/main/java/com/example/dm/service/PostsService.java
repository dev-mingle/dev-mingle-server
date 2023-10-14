package com.example.dm.service;

import com.example.dm.dto.posts.PostDetailInfoDto;
import com.example.dm.entity.Images;
import com.example.dm.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostsService {

    Page<Posts> findAll(Long categoryId, String search, String[] conditions, double[] location, Pageable pageable);

    PostDetailInfoDto findById(Long postsId);

    Long update(Long postsId, Posts posts, List<Images> images);
}
