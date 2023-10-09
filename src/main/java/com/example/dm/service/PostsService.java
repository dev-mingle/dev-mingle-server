package com.example.dm.service;

import com.example.dm.dto.posts.PostDetailInfoDto;
import com.example.dm.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostsService {

    Page<Posts> findAll(Long categoryId, String search, String[] conditions, double[] location, Pageable pageable);

    PostDetailInfoDto findById(Long postsId);
}
