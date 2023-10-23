package com.example.dm.controller;

import com.example.dm.dto.images.ImagesDto;
import com.example.dm.dto.posts.PostSaveDto;
import com.example.dm.dto.posts.PostsUpdateDto;
import com.example.dm.entity.Categories;
import com.example.dm.repository.UserProfileRepository;
import com.example.dm.security.jwt.TokenFilter;
import com.example.dm.service.CategoriesService;
import com.example.dm.service.PostsService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @MockBean
    private UserProfileRepository userProfileRepository;
    @MockBean
    private CategoriesService categoriesService;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("게시글 목록 조회")
    @Test
    void findAl() throws Exception {
        // given
        Categories categories = Categories.builder().id(1L).build();
        given(categoriesService.getCategories("all")).willReturn(categories);
        given(postsService.findAll(
                1L,
                "backend",
                List.of("title", "content"),
                37.619774,
                127.060926,
                PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"))))
                .willReturn(Page.empty());

        // expected
        mockMvc.perform(get("/api/v1/posts")
                        .param("category", "all")
                        .param("search", "backend")
                        .param("conditions", "title,content")
                        .param("latitude", "37.619774")
                        .param("longitude", "127.060926")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "createdAt,desc")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("200"))
                .andDo(print());
    }

    @DisplayName("게시글 목록 조회 카테고리는 필수값")
    @Test
    void findAl_category() throws Exception {
        // expected
        mockMvc.perform(get("/api/v1/posts")
                        .param("search", "backend")
                        .param("conditions", "title,content")
                        .param("latitude", "37.619774")
                        .param("longitude", "127.060926")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "createdAt,desc")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("category:must not be blank "))
                .andDo(print());
    }

    @DisplayName("게시글 목록 조회 위치정보 위도는 필수값")
    @Test
    void findAl_latitude() throws Exception {
        // expected
        mockMvc.perform(get("/api/v1/posts")
                        .param("category", "all")
                        .param("search", "backend")
                        .param("conditions", "title,content")
                        .param("longitude", "127.060926")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "createdAt,desc")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("latitude:must not be null "))
                .andDo(print());
    }

    @DisplayName("게시글 목록 조회 위치정보 경도는 필수값")
    @Test
    void findAl_longitude() throws Exception {
        // expected
        mockMvc.perform(get("/api/v1/posts")
                        .param("category", "all")
                        .param("search", "backend")
                        .param("conditions", "title,content")
                        .param("latitude", "37.619774")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "createdAt,desc")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("longitude:must not be null "))
                .andDo(print());
    }

    @DisplayName("게시글 목록 조회 페이지 기본 설정은 페이지 0 사이즈 20")
    @Test
    void findAll_page() throws Exception {
        // given
        Categories categories = Categories.builder().id(1L).build();
        given(categoriesService.getCategories("all")).willReturn(categories);
        given(postsService.findAll(
                1L,
                "backend",
                List.of("title", "content"),
                37.619774,
                127.060926,
                PageRequest.of(0, 20)))
                .willReturn(Page.empty());

        // expected
        mockMvc.perform(get("/api/v1/posts")
                        .param("category", "all")
                        .param("search", "backend")
                        .param("conditions", "title,content")
                        .param("latitude", "37.619774")
                        .param("longitude", "127.060926")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("게시글 저장시 카테고리 필수 값")
    @Test
    void save_category() throws Exception {
        // given
        PostSaveDto postSaveDto = PostSaveDto.builder()
                .title("test_title")
                .contents("test_content")
                .latitude(37.619774)
                .longitude(127.060926)
                .hasChat(true)
                .imageUrl(List.of("test_image1", "test_image2"))
                .build();
        String json = objectMapper.writeValueAsString(postSaveDto);

        // expected
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("category:must not be blank "))
                .andDo(print());
    }

    @DisplayName("게시글 저장시 제목 필수 값")
    @Test
    void save_title() throws Exception {
        // given
        PostSaveDto postSaveDto = PostSaveDto.builder()
                .category("test_category")
                .contents("test_content")
                .latitude(37.619774)
                .longitude(127.060926)
                .hasChat(true)
                .imageUrl(List.of("test_image1", "test_image2"))
                .build();
        String json = objectMapper.writeValueAsString(postSaveDto);

        // expected
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("title:must not be blank "))
                .andDo(print());
    }

    @DisplayName("게시글 저장시 내용 필수 값")
    @Test
    void save_content() throws Exception {
        // given
        PostSaveDto postSaveDto = PostSaveDto.builder()
                .category("test_category")
                .title("test_title")
                .latitude(37.619774)
                .longitude(127.060926)
                .hasChat(true)
                .imageUrl(List.of("test_image1", "test_image2"))
                .build();
        String json = objectMapper.writeValueAsString(postSaveDto);

        // expected
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("contents:must not be blank "))
                .andDo(print());
    }

    @DisplayName("게시글 저장시 위도 필수 값")
    @Test
    void save_latitude() throws Exception {
        // given
        PostSaveDto postSaveDto = PostSaveDto.builder()
                .category("test_category")
                .title("test_title")
                .contents("test_content")
                .longitude(127.060926)
                .hasChat(true)
                .imageUrl(List.of("test_image1", "test_image2"))
                .build();
        String json = objectMapper.writeValueAsString(postSaveDto);

        // expected
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("latitude:must not be null "))
                .andDo(print());
    }

    @DisplayName("게시글 저장시 경도 필수 값")
    @Test
    void save_longitude() throws Exception {
        // given
        PostSaveDto postSaveDto = PostSaveDto.builder()
                .category("test_category")
                .title("test_title")
                .contents("test_content")
                .latitude(37.619774)
                .hasChat(true)
                .imageUrl(List.of("test_image1", "test_image2"))
                .build();
        String json = objectMapper.writeValueAsString(postSaveDto);

        // expected
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("longitude:must not be null "))
                .andDo(print());
    }

    @DisplayName("게시물 수정 카테고리 필수 값")
    @Test
    void update_category() throws Exception {
        // given
        ImagesDto imagesDto = ImagesDto.builder()
                .url("test_image_url2")
                .build();
        PostsUpdateDto postsUpdateDto = PostsUpdateDto.builder()
                .title("test_title")
                .contents("test_content")
                .hasChat(true)
                .images(List.of(imagesDto))
                .build();
        String json = objectMapper.writeValueAsString(postsUpdateDto);

        // expected
        mockMvc.perform(put("/api/v1/posts/{postId}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("category:must not be blank "))
                .andDo(print());
    }

    @DisplayName("게시물 수정 제목 필수 값")
    @Test
    void update_title() throws Exception {
        // given
        ImagesDto imagesDto = ImagesDto.builder()
                .url("test_image_url2")
                .build();
        PostsUpdateDto postsUpdateDto = PostsUpdateDto.builder()
                .category("test_category")
                .contents("test_content")
                .hasChat(true)
                .images(List.of(imagesDto))
                .build();
        String json = objectMapper.writeValueAsString(postsUpdateDto);

        // expected
        mockMvc.perform(put("/api/v1/posts/{postId}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("title:must not be blank "))
                .andDo(print());
    }

    @DisplayName("게시물 수정 내용 필수 값")
    @Test
    void update_contents() throws Exception {
        // given
        ImagesDto imagesDto = ImagesDto.builder()
                .url("test_image_url2")
                .build();
        PostsUpdateDto postsUpdateDto = PostsUpdateDto.builder()
                .category("test_category")
                .title("test_title")
                .hasChat(true)
                .images(List.of(imagesDto))
                .build();
        String json = objectMapper.writeValueAsString(postsUpdateDto);

        // expected
        mockMvc.perform(put("/api/v1/posts/{postId}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("contents:must not be blank "))
                .andDo(print());
    }

    @DisplayName("게시물 수정 이미지 url 필수 값")
    @Test
    void update_image_url() throws Exception {
        // given
        ImagesDto imagesDto = ImagesDto.builder()
                .build();
        PostsUpdateDto postsUpdateDto = PostsUpdateDto.builder()
                .category("test_category")
                .title("test_title")
                .contents("test_content")
                .hasChat(true)
                .images(List.of(imagesDto))
                .build();
        String json = objectMapper.writeValueAsString(postsUpdateDto);

        // expected
        mockMvc.perform(put("/api/v1/posts/{postId}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("images[0].url:must not be empty "))
                .andDo(print());
    }
}