package com.example.dm.controller;

import com.example.dm.security.jwt.TokenFilter;
import com.example.dm.service.PostsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostsController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = TokenFilter.class))
class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostsService postsService;

    @Test
    @DisplayName("게시글 목록 조회 PostListDto, ApiResponse")
    void findAll() throws Exception {
        // given
        given(postsService.findAll(
                        eq(1L),
                        eq("backend"),
                        eq(new String[]{"title", "content"}),
                        eq(new double[]{37.619774, 127.060926}),
                        eq(PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt")))))
                .willReturn(Page.empty());

        // expected
        mockMvc.perform(get("/api/v1/posts/{categoryId}", 1)
                        .param("search", "backend")
                        .param("conditions", "title,content")
                        .param("location", "37.619774,127.060926")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "createdAt,desc")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("정상적으로 처리되었습니다."))
                .andExpect(jsonPath("$.data.content").isEmpty())
                .andExpect(jsonPath("$.data.number").value("0"))
                .andExpect(jsonPath("$.data.size").value("0"))
                .andDo(print());
    }

    @Test
    @DisplayName("validation check categoryId:must not be nul")
    void findAll_category_id_null() throws Exception {
        // expected
        mockMvc.perform(get("/api/v1/posts/{categoryId}", " ")
                        .param("search", "backend")
                        .param("conditions", "title,content")
                        .param("location", "37.619774,127.060926")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "createdAt,desc")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("422"))
                .andDo(print());
    }

    @Test
    @DisplayName("validation check categoryId:Failed to convert")
    void findAll_category_id_string() throws Exception {
        // expected
        mockMvc.perform(get("/api/v1/posts/{categoryId}", "aaa")
                        .param("search", "backend")
                        .param("conditions", "title,content")
                        .param("location", "37.619774,127.060926")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "createdAt,desc")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("422"))
                .andDo(print());
    }

    @Test
    @DisplayName("validation check location: size must be between 2 and 2")
    void findAl_location_size() throws Exception {
        // expected
        mockMvc.perform(get("/api/v1/posts/{categoryId}", "1")
                        .param("search", "backend")
                        .param("conditions", "title,content")
                        .param("location", "")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "createdAt,desc")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("422"))
                .andDo(print());
    }

    @Test
    @DisplayName("validation check location:Failed to convert")
    void findAl_location_string() throws Exception {
        // expected
        mockMvc.perform(get("/api/v1/posts/{categoryId}", "1")
                        .param("search", "backend")
                        .param("conditions", "title,content")
                        .param("location", "37.619774,aaa")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "createdAt,desc")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("422"))
                .andDo(print());
    }
}