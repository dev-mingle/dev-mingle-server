package com.example.dm.service;

import com.example.dm.entity.Posts;
import com.example.dm.exception.BadApiRequestException;
import com.example.dm.repository.PostsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PostsServiceTest {

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

    @DisplayName("수정을 통해 기존에 존재하는 채팅방을 삭제 시도하면 예외 발생")
    @Test
    void update_chat_delete() {
        // given
        Posts posts = Posts.builder().hasChat(true).build();
        Posts updatePosts = Posts.builder().hasChat(false).build();
        given(postsRepository.getPostsWithOptimisticLock(1L)).willReturn(posts);

        // expected
        assertThatThrownBy(() -> postsService.update(1L, updatePosts, null))
                .isInstanceOf(BadApiRequestException.class)
                .hasMessage("잘못된 요청입니다. [채팅방 삭제는 불가능합니다.]");
    }
}