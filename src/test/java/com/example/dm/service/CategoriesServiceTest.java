package com.example.dm.service;

import com.example.dm.entity.Categories;
import com.example.dm.repository.CategoriesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriesServiceTest {

    @InjectMocks
    private CategoriesServiceImpl categoriesService;
    @Mock
    private CategoriesRepository categoriesRepository;

    @DisplayName("카테고리 아이디로 조회할 때 처음 한 번만 repository 조회하고 두 번째부터는 캐시에서 조회")
    @Test
    void getCategories_by_id() {
        // given
        Categories category = Categories.builder().id(1L).name("test_category").build();
        given(categoriesRepository.findById(1L)).willReturn(Optional.of(category));

        // when
        Categories categories1 = categoriesService.getCategories(1L);
        Categories categories2 = categoriesService.getCategories(1L);

        // then
        then(categoriesRepository).should().findById(1L);
        assertThat(categories1).isSameAs(categories2);
    }

    @DisplayName("카테고리 아이디로 조회할 아이디가 -1 보다 작으면 -1로 조회")
    @Test
    void getCategories_id_lt_minus_one() {
        // given
        Categories category = Categories.builder().id(-1L).name("test_category").build();
        given(categoriesRepository.findById(any())).willReturn(Optional.of(category));

        // when
        Categories categories1 = categoriesService.getCategories(-5L);
        Categories categories2 = categoriesService.getCategories(-10L);

        // then
        then(categoriesRepository).should().findById(-1L);
        assertThat(categories1).isSameAs(categories2);
    }

    @DisplayName("카테고리 이름으로 조회할 때 처음 한 번만 repository 조회하고 두 번째부터는 캐시에서 조회")
    @Test
    void getCategories_by_name() {
        // given
        Categories category = Categories.builder().id(1L).name("test_category").build();
        given(categoriesRepository.findByName("test_category")).willReturn(Optional.of(category));

        // when
        Categories categories1 = categoriesService.getCategories("test_category");
        Categories categories2 = categoriesService.getCategories("test_category");

        // then
        then(categoriesRepository).should().findByName("test_category");
        assertThat(categories1).isSameAs(categories2);
    }
}