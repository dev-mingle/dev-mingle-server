package com.example.dm.controller;

import com.example.dm.annotation.ApiResponseBody;
import com.example.dm.dto.posts.PostListDto;
import com.example.dm.dto.posts.PostListInfoDto;
import com.example.dm.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@ApiResponseBody
@RequiredArgsConstructor
@RequestMapping(value = "${api.path.default}/posts")
class PostsController{

    private final PostsService postsService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{categoryId}")
    Page<PostListInfoDto> findAll(@Validated PostListDto postListDto, Pageable pageable) {
        return postsService.findAll(postListDto.categoryId(), postListDto.search(), postListDto.conditions(), postListDto.location(), pageable)
                .map(PostListInfoDto::convertPosts);
    }
}
