package com.example.dm.post.controller;

import com.example.dm.controller.BaseController;
import com.example.dm.dto.ApiResponse;
import com.example.dm.post.entity.Posts;
import com.example.dm.post.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "${api.path.default}/posts")
class PostsController extends BaseController {

    private final PostsService postsService;

    @GetMapping
    ResponseEntity<ApiResponse> findAll(String search, String[] conditions, String[] location, Pageable pageable) {
        Page<Posts> posts = postsService.findAll(search, conditions, location, pageable);
        return responseBuilder(posts, HttpStatus.OK);
    }
}
