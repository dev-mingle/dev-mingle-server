package com.example.dm.service;

import com.example.dm.repository.PostsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PostsServiceImplTest {

    @InjectMocks
    private PostsServiceImpl postsService;

    @Mock
    private PostsRepository postsRepository;


    @Test
    void findAll() {
        // given
        // when
        postsService.findAll(
                1L,
                "search",
                new String[]{"title, content"},
                new double[]{1.0, 2.0},
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")));

        // then
        then(postsRepository).should()
                .findAll(1L,
                        "search",
                        new String[]{"title, content"},
                        new double[]{1.0, 2.0},
                        0.03,
                        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @DisplayName("기본 페이지 size = 100 number = 0 sort = createdAt,desc")
    @Test
    void findAll_page_null() {
        // given
        // when
        postsService.findAll(
                1L,
                "search",
                new String[]{"title, content"},
                new double[]{1.0, 2.0},
                null);

        // then
        then(postsRepository).should()
                .findAll(1L,
                        "search",
                        new String[]{"title, content"},
                        new double[]{1.0, 2.0},
                        0.03,
                        PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @DisplayName("기본 정렬 sort = createdAt,desc")
    @Test
    void findAll_sort_null() {
        // given
        // when
        postsService.findAll(
                1L,
                "search",
                new String[]{"title, content"},
                new double[]{1.0, 2.0},
                PageRequest.of(0, 10));

        // then
        then(postsRepository).should()
                .findAll(1L,
                        "search",
                        new String[]{"title, content"},
                        new double[]{1.0, 2.0},
                        0.03,
                        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")));
    }
}