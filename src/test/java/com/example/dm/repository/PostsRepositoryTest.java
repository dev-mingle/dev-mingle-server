package com.example.dm.repository;

import com.example.dm.entity.Posts;
import com.example.dm.exception.BadApiRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Sql("/sql/post/posts_test_data.sql")
@Import(PostsRepositoryImpl.class)
class PostsRepositoryTest {

    @Autowired
    private PostsRepository postsRepository;

    private final static Pageable PAGE = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));

    private final static double DISTANCE = 0.03;

    @DisplayName("게시글 목록 전체 조회")
    @Test
    void findAll() {
        // given
        // when
        Page<Posts> postsPage = postsRepository.findAll(-1L, null, null, 37.481445, 126.952688, DISTANCE, PAGE);

        // then
        List<String> titleList = postsPage.getContent().stream().map(Posts::getTitle).toList();
        assertThat(titleList).containsExactlyInAnyOrder("신림2", "낙성대");
    }

    @DisplayName("게시글 목록 전체 조회시 위도 경도 위치 범위에 존재하는 데이터가 없으면 빈 리스트 반환")
    @Test
    void findAll_location() {
        // given
        // when
        Page<Posts> postsPage = postsRepository.findAll(-1L, null, null, 137.481445, 226.952688, DISTANCE, PAGE);

        // then
        assertThat(postsPage).isEmpty();
    }

    @DisplayName("제목이 '신'을 포함하는 데이터 조회")
    @Test
    void findAll_with_search_title() {
        // given
        // when
        Page<Posts> postsPage = postsRepository.findAll(-1L, "신", List.of("title"), 37.481445, 126.952688, DISTANCE, PAGE);

        // then
        List<String> titleList = postsPage.getContent().stream().map(Posts::getTitle).toList();
        assertThat(titleList).containsExactlyInAnyOrder("신림", "신림2");
    }

    @DisplayName("내용이 '신'을 포함하는 데이터 조회")
    @Test
    void findAll_with_search_content() {
        // given
        // when
        Page<Posts> postsPage = postsRepository.findAll(-1L, "신", List.of("contents"), 37.481445, 126.952688, DISTANCE, PAGE);

        // then
        List<String> titleList = postsPage.getContent().stream().map(Posts::getTitle).toList();
        assertThat(titleList).containsExactlyInAnyOrder("신림", "신림2");
    }

    @DisplayName("카테고리 아이디 같은 게시글 조회")
    @Test
    void findAll_with_category() {
        // given
        // when
        Page<Posts> postsPage = postsRepository.findAll(1L, null, null, 37.481445, 126.952688, DISTANCE, PAGE);

        // then
        List<String> titleList = postsPage.getContent().stream().map(Posts::getTitle).toList();
        assertThat(titleList).containsExactlyInAnyOrder("낙성대", "신림");
    }

    @DisplayName("존재하지 않는 카테고리아이디로 조회시 결과 0")
    @Test
    void findAll_categoryId() {
        // given
        // when
        Page<Posts> postsPage = postsRepository.findAll(3L, null, null, 37.481445, 126.952688, DISTANCE, PAGE);

        // then
        assertThat(postsPage.getContent()).isEmpty();
    }

    @DisplayName("정렬 조건 여러개 조회")
    @Test
    void findAll_multi_sort() {
        // given
        ArrayList<Sort.Order> orders = new ArrayList<>();
        orders.add(Sort.Order.asc("contents"));
        orders.add(Sort.Order.desc("createdAt"));
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.by(orders));

        // when
        Page<Posts> postsPage = postsRepository.findAll(-1L, null, null, 37.481445, 126.952688, DISTANCE, pageRequest);

        // then
        List<String> titleList = postsPage.getContent().stream().map(Posts::getTitle).toList();
        assertThat(titleList).containsExactlyInAnyOrder("낙성대", "서울대입구", "신림2", "신림");
    }

    @DisplayName("틀린 정렬 조건 조회")
    @Test
    void findAll_sort_false() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "created_at"));

        // when
        assertThatThrownBy(() -> postsRepository.findAll(-1L, null, null, 37.481445, 126.952688, DISTANCE, pageRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("게시물 조회시 id에 해당하는 게시물이 없으면 에러 발생")
    @Test
    void getPosts() {
        // expected
        assertThatThrownBy(() -> postsRepository.getPosts(100L))
                .isInstanceOf(BadApiRequestException.class)
                .hasMessage("잘못된 요청입니다. [100: This post doesn't exist]");
    }

    @DisplayName("게시물 조회(락)시 id에 해당하는 게시물이 없으면 에러 발생")
    @Test
    void getPostsWithOptimisticLock() {
        // expected
        assertThatThrownBy(() -> postsRepository.getPostsWithOptimisticLock(100L))
                .isInstanceOf(BadApiRequestException.class)
                .hasMessage("잘못된 요청입니다. [100: This post doesn't exist]");
    }
}