package com.example.dm.service;

import com.example.dm.annotation.UpdateRetry;
import com.example.dm.dto.posts.PostsAndImages;
import com.example.dm.entity.Images;
import com.example.dm.entity.Posts;
import com.example.dm.enums.ImageType;
import com.example.dm.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;
    private final ImagesService imagesService;

    // 0.01(degrees)는 약 1.11km(distance)
    private static final double DISTANCE = 0.03;
    private static final int MAX_PAGE_SIZE = 100;

    private static final Pageable DEFAULT_PAGE = PageRequest.of(
            0,
            MAX_PAGE_SIZE,
            Sort.by(Sort.Direction.DESC, "createdAt")
    );

    @Transactional(readOnly = true)
    @Override
    public Page<Posts> findAll(Long categoryId, String search, List<String> conditions, double latitude, double longitude, Pageable pageable) {
        if (pageable == null) {
            pageable = DEFAULT_PAGE;
        } else if (pageable.getSort().isEmpty()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), DEFAULT_PAGE.getSort());
        }

        if (pageable.getPageSize() > MAX_PAGE_SIZE) {
            pageable = PageRequest.of(pageable.getPageNumber(), MAX_PAGE_SIZE, pageable.getSort());
        }

        return postsRepository.findAll(categoryId, search, conditions, latitude, longitude, DISTANCE, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public PostsAndImages findById(Long postsId) {
        Posts posts = postsRepository.getPosts(postsId);
        List<Images> images = imagesService.findAllByReferenceIdAndType(postsId, ImageType.Posts);
        return new PostsAndImages(posts, images);
    }

    @Override
    public Long save(Posts posts, List<String> imagesUrlList) {
        Long postId = postsRepository.save(posts);
        List<Images> toSaveImages = imagesUrlList.stream()
                .map(url -> Images.create(url, ImageType.Posts, postId))
                .toList();
        imagesService.saveAll(toSaveImages);
        return postId;
    }

    @UpdateRetry
    @Override
    public Long update(Long postsId, Posts posts, List<Images> images) {
        Posts findPosts = postsRepository.getPostsWithOptimisticLock(postsId);
        findPosts.change(posts);
        imagesService.update(postsId, ImageType.Posts, images);
        return postsId;
    }

    @UpdateRetry
    @Override
    public Long delete(Long postsId) {
        Posts findPosts = postsRepository.getPostsWithOptimisticLock(postsId);
        findPosts.delete();
        imagesService.delete(postsId, ImageType.Posts);
        return postsId;
    }
}