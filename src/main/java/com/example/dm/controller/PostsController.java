package com.example.dm.controller;

import com.example.dm.annotation.ApiResponseBody;
import com.example.dm.dto.posts.*;
import com.example.dm.entity.*;
import com.example.dm.exception.BadApiRequestException;
import com.example.dm.repository.UserProfileRepository;
import com.example.dm.service.CategoriesService;
import com.example.dm.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final UserProfileRepository userProfileRepository;
    private final CategoriesService categoriesService;


    @GetMapping
    Page<PostListInfoDto> findAll(@Validated PostListDto postListDto, Pageable pageable) {
        String category = postListDto.category();
        Long categoryId = categoriesService.getCategories(category).getId();

        Page<Posts> postsPage = postsService.findAll(
                categoryId,
                postListDto.search(),
                postListDto.conditions(),
                postListDto.latitude(),
                postListDto.longitude(),
                pageable);
        return postsPage.map(p -> PostListInfoDto.convertFromPosts(p, category));
    }

    @GetMapping("/{postsId}")
    PostDetailInfoDto findDetail(@PathVariable Long postsId) {
        PostsAndImages postsAndImages = postsService.findById(postsId);
        Long categoryId = postsAndImages.posts().getCategory().getId();
        Categories categories = categoriesService.getCategories(categoryId);
        return PostDetailInfoDto.convertFromPostsAndImages(postsAndImages, categories.getName());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Long save(@Validated @RequestBody PostSaveDto postSaveDto, @AuthenticationPrincipal LoginUser loginUser) {
        UserProfiles userProfiles = userProfileRepository.findById(loginUser.getUserProfileId())
                .orElseThrow(() -> new BadApiRequestException("유저를 찾을 수 없습니다."));
        Categories categories = categoriesService.getCategories(postSaveDto.getCategory());

        Posts posts = postSaveDto.convertToPosts(userProfiles, categories);
        List<String> imagesUrlList = postSaveDto.getImageUrl();

        return postsService.save(posts, imagesUrlList);
    }

    @PutMapping("/{postsId}")
    Long update(@PathVariable Long postsId, @RequestBody @Validated PostsUpdateDto postsUpdateDto) {
        Categories categories = categoriesService.getCategories(postsUpdateDto.getCategory());

        Posts posts = postsUpdateDto.convertPosts(categories);
        List<Images> images = postsUpdateDto.getImages(postsId);

        return postsService.update(postsId, posts, images);
    }

    @DeleteMapping("/{postsId}")
    Long delete(@PathVariable Long postsId) {
        return postsService.delete(postsId);
    }
}
