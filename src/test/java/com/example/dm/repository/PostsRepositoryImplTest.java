package com.example.dm.repository;

import com.example.dm.entity.Posts;
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
class PostsRepositoryImplTest {

    @Autowired
    private PostsRepository postsRepository;

    private final static Pageable PAGE = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));

    private final static double DISTANCE = 0.03;

    @DisplayName("게시글 목록 전체 조회")
    @Test
    void findAll() {
        // given
        // when
        Page<Posts> postsPage = postsRepository.findAll(-1L, null, null, new double[]{37.481445, 126.952688}, DISTANCE, PAGE);

        // then
        List<String> titleList = postsPage.getContent().stream().map(Posts::getTitle).toList();
        assertThat(titleList).containsExactlyInAnyOrder("신림2", "낙성대");
    }

    @DisplayName("제목이 '신'을 포함하는 데이터 조회")
    @Test
    void findAll_with_search_title() {
        // given
        // when
        Page<Posts> postsPage = postsRepository.findAll(-1L, "신", new String[]{"title"}, new double[]{37.481445, 126.952688}, DISTANCE, PAGE);

        // then
        List<String> titleList = postsPage.getContent().stream().map(Posts::getTitle).toList();
        assertThat(titleList).containsExactlyInAnyOrder("신림", "신림2");
    }

    @DisplayName("내용이 '신'을 포함하는 데이터 조회")
    @Test
    void findAll_with_search_content() {
        // given
        // when
        Page<Posts> postsPage = postsRepository.findAll(-1L, "신", new String[]{"contents"}, new double[]{37.481445, 126.952688}, DISTANCE, PAGE);

        // then
        List<String> titleList = postsPage.getContent().stream().map(Posts::getTitle).toList();
        assertThat(titleList).containsExactlyInAnyOrder("신림", "신림2");
    }

    @DisplayName("카테고리 아이디 같은 게시글 조회")
    @Test
    void findAll_with_category() {
        // given
        // when
        Page<Posts> postsPage = postsRepository.findAll(1L, null, null, new double[]{37.481445, 126.952688}, DISTANCE, PAGE);

        // then
        List<String> titleList = postsPage.getContent().stream().map(Posts::getTitle).toList();
        assertThat(titleList).containsExactlyInAnyOrder("낙성대", "신림");
    }

    @DisplayName("존재하지 않는 카테고리아이디로 조회시 결과 0")
    @Test
    void findAll_categoryId() {
        // given
        // when
        Page<Posts> postsPage = postsRepository.findAll(3L, null, null, new double[]{37.481445, 126.952688}, DISTANCE, PAGE);

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
        Page<Posts> postsPage = postsRepository.findAll(-1L, null, null, new double[]{37.481445, 126.952688}, DISTANCE, pageRequest);

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
        assertThatThrownBy(() -> postsRepository.findAll(-1L, null, null, new double[]{37.481445, 126.952688}, DISTANCE, pageRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}