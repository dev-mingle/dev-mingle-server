package com.example.dm.controller;

import com.example.dm.dto.images.ImagesDto;
import com.example.dm.dto.posts.PostDetailInfoDto;
import com.example.dm.dto.posts.PostsUpdateDto;
import com.example.dm.entity.Posts;
import com.example.dm.enums.ImageType;
import com.example.dm.security.jwt.TokenFilter;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    void findById() throws Exception{
        // given
        PostDetailInfoDto dto = PostDetailInfoDto.builder().build();
        given(postsService.findById(1L)).willReturn(dto);

        // expected
        mockMvc.perform(get("/api/v1/posts/detail/{postId}", "1")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("200"))
                .andDo(print());
    }

    @Test
    void update() throws Exception {
        // given
        ImagesDto imagesDto = ImagesDto.builder()
                .id(1L)
                .url("images1")
                .type(ImageType.Posts)
                .referenceId(1L)
                .build();
        List<ImagesDto> imagesDtoList = List.of(imagesDto);
        PostsUpdateDto postsDto = PostsUpdateDto.builder()
                .categoryId(1L)
                .title("post_title")
                .contents("post_content")
                .hasChat(true)
                .images(imagesDtoList)
                .build();
        String json = objectMapper.writeValueAsString(postsDto);
        given(postsService.update(eq(1L), any(Posts.class), any())).willReturn(1L);

        // expected
        mockMvc.perform(put("/api/v1/posts/detail/{postId}", "1")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("200"))
                .andDo(print());
    }

    @DisplayName("게시물 수정시 카테고리 아이디가 비었으면 예외 발생")
    @Test
    void update_categoryId_empty() throws Exception {
        // given
        ImagesDto imagesDto = ImagesDto.builder()
                .id(1L)
                .url("images1")
                .type(ImageType.Posts)
                .referenceId(1L)
                .build();
        List<ImagesDto> imagesDtoList = List.of(imagesDto);
        PostsUpdateDto postsDto = PostsUpdateDto.builder()
                .title("post_title")
                .contents("post_content")
                .hasChat(true)
                .images(imagesDtoList)
                .build();
        String json = objectMapper.writeValueAsString(postsDto);
        given(postsService.update(eq(1L), any(Posts.class), any())).willReturn(1L);

        // expected
        mockMvc.perform(put("/api/v1/posts/detail/{postId}", "1")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("422"))
                .andExpect(jsonPath("$.message").value("categoryId:must not be null "))
                .andDo(print());
    }

    @DisplayName("게시물 수정시 타이틀이 비었으면 예외 발생")
    @Test
    void update_title_empty() throws Exception {
        // given
        ImagesDto imagesDto = ImagesDto.builder()
                .id(1L)
                .url("images1")
                .type(ImageType.Posts)
                .referenceId(1L)
                .build();
        List<ImagesDto> imagesDtoList = List.of(imagesDto);
        PostsUpdateDto postsDto = PostsUpdateDto.builder()
                .categoryId(1L)
                .contents("post_content")
                .hasChat(true)
                .images(imagesDtoList)
                .build();
        String json = objectMapper.writeValueAsString(postsDto);
        given(postsService.update(eq(1L), any(Posts.class), any())).willReturn(1L);

        // expected
        mockMvc.perform(put("/api/v1/posts/detail/{postId}", "1")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("422"))
                .andExpect(jsonPath("$.message").value("title:must not be empty "))
                .andDo(print());
    }

    @DisplayName("게시물 수정시 컨텐츠가 비었으면 예외 발생")
    @Test
    void update_contents_empty() throws Exception {
        // given
        ImagesDto imagesDto = ImagesDto.builder()
                .id(1L)
                .url("images1")
                .type(ImageType.Posts)
                .referenceId(1L)
                .build();
        List<ImagesDto> imagesDtoList = List.of(imagesDto);
        PostsUpdateDto postsDto = PostsUpdateDto.builder()
                .categoryId(1L)
                .title("post_title")
                .hasChat(true)
                .images(imagesDtoList)
                .build();
        String json = objectMapper.writeValueAsString(postsDto);
        given(postsService.update(eq(1L), any(Posts.class), any())).willReturn(1L);

        // expected
        mockMvc.perform(put("/api/v1/posts/detail/{postId}", "1")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("422"))
                .andExpect(jsonPath("$.message").value("contents:must not be empty "))
                .andDo(print());
    }

    @DisplayName("게시물 수정 이미지 목록중 url 없는 이미지가 있으면 예외 발생")
    @Test
    void update_image_url_empty() throws Exception {
        // given
        ImagesDto imagesDto = ImagesDto.builder()
                .id(1L)
                .type(ImageType.Posts)
                .referenceId(1L)
                .build();
        List<ImagesDto> imagesDtoList = List.of(imagesDto);
        PostsUpdateDto postsDto = PostsUpdateDto.builder()
                .categoryId(1L)
                .title("post_title")
                .contents("post_content")
                .hasChat(true)
                .images(imagesDtoList)
                .build();
        String json = objectMapper.writeValueAsString(postsDto);
        given(postsService.update(eq(1L), any(Posts.class), any())).willReturn(1L);

        // expected
        mockMvc.perform(put("/api/v1/posts/detail/{postId}", "1")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("422"))
                .andExpect(jsonPath("$.message").value("images[0].url:must not be empty "))
                .andDo(print());
    }

    @DisplayName("게시물 수정 이미지 목록중 type 없는 이미지가 있으면 예외 발생")
    @Test
    void update_image_type_empty() throws Exception {
        // given
        ImagesDto imagesDto = ImagesDto.builder()
                .id(1L)
                .url("images1")
                .referenceId(1L)
                .build();
        List<ImagesDto> imagesDtoList = List.of(imagesDto);
        PostsUpdateDto postsDto = PostsUpdateDto.builder()
                .categoryId(1L)
                .title("post_title")
                .contents("post_content")
                .hasChat(true)
                .images(imagesDtoList)
                .build();
        String json = objectMapper.writeValueAsString(postsDto);
        given(postsService.update(eq(1L), any(Posts.class), any())).willReturn(1L);

        // expected
        mockMvc.perform(put("/api/v1/posts/detail/{postId}", "1")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("422"))
                .andExpect(jsonPath("$.message").value("images[0].type:must not be null "))
                .andDo(print());
    }

    @DisplayName("게시물 수정 이미지 목록중 referenceId 없는 이미지가 있으면 예외 발생")
    @Test
    void update_image_referenceId_empty() throws Exception {
        // given
        ImagesDto imagesDto = ImagesDto.builder()
                .id(1L)
                .url("images1")
                .type(ImageType.Posts)
                .build();
        List<ImagesDto> imagesDtoList = List.of(imagesDto);
        PostsUpdateDto postsDto = PostsUpdateDto.builder()
                .categoryId(1L)
                .title("post_title")
                .contents("post_content")
                .hasChat(true)
                .images(imagesDtoList)
                .build();
        String json = objectMapper.writeValueAsString(postsDto);
        given(postsService.update(eq(1L), any(Posts.class), any())).willReturn(1L);

        // expected
        mockMvc.perform(put("/api/v1/posts/detail/{postId}", "1")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("422"))
                .andExpect(jsonPath("$.message").value("images[0].referenceId:must not be null "))
                .andDo(print());
    }
}