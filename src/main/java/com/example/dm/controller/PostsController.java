package com.example.dm.controller;

import com.example.dm.annotation.ApiResponseBody;
import com.example.dm.dto.images.ImagesDto;
import com.example.dm.dto.posts.PostDetailInfoDto;
import com.example.dm.dto.posts.PostListDto;
import com.example.dm.dto.posts.PostListInfoDto;
import com.example.dm.dto.posts.PostsUpdateDto;
import com.example.dm.entity.Images;
import com.example.dm.entity.Posts;
import com.example.dm.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ApiResponseBody
@RequiredArgsConstructor
@RequestMapping(value = "${api.path.default}/posts")
class PostsController{

    private final PostsService postsService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{categoryId}")
    Page<PostListInfoDto> findAll(@PathVariable Long categoryId, @Validated PostListDto postListDto, Pageable pageable) {
        return postsService.findAll(
                        categoryId,
                        postListDto.search(),
                        postListDto.conditions(),
                        postListDto.location(),
                        pageable)
                .map(PostListInfoDto::convertPosts);
    }

    @GetMapping("/detail/{postsId}")
    PostDetailInfoDto findDetail(@PathVariable Long postsId) {
        return postsService.findById(postsId);
    }

    @PutMapping("/detail/{postsId}")
    Long update(@PathVariable Long postsId, @RequestBody @Validated PostsUpdateDto postsUpdateDto) {
        Posts posts = postsUpdateDto.convertPosts();
        List<Images> images = postsUpdateDto.getImages()
                .stream()
                .map(ImagesDto::convertToImages)
                .toList();
        return postsService.update(postsId, posts, images);
    }
}
