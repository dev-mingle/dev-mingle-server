package com.example.dm.service;

import com.example.dm.entity.Posts;
import com.example.dm.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;

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
    public Page<Posts> findAll(Long categoryId, String search, String[] conditions, double[] location, Pageable pageable) {
        if (pageable == null) {
            pageable = DEFAULT_PAGE;
        } else if (pageable.getSort().isEmpty()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), DEFAULT_PAGE.getSort());
        }

        if (pageable.getPageSize() > MAX_PAGE_SIZE) {
            pageable = PageRequest.of(pageable.getPageNumber(), MAX_PAGE_SIZE, pageable.getSort());
        }

        return postsRepository.findAll(categoryId, search, conditions, location, DISTANCE, pageable);
    }
}
