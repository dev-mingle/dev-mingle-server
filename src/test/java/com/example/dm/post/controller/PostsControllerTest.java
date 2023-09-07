package com.example.dm.post.controller;

import com.example.dm.post.service.PostsService;
import com.example.dm.util.TxidGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostsController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(TxidGenerator.class)
class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostsService postsService;

    @Test
    @DisplayName("게시글 목록 조회시 데이터가 없을 경우 빈 리스트 반환")
    void findAll_empty() throws Exception {
        // given
        String search = "backend";
        String conditions = "title,content";
        String location = "cl,37.619774,127.060926";
        String page = "0";
        String size = "20";
        String sort = "createdAt,desc";
        PageRequest pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        given(postsService.findAll(anyLong(), anyString(), any(), any(), any())).willReturn(Page.empty(pageable));

        // when
        mockMvc.perform(get("/api/v1/posts/{categoryId}", 1)
                        .param("search", search)
                        .param("conditions", conditions)
                        .param("location", location)
                        .param("page", page)
                        .param("size", size)
                        .param("sort", sort)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.txid").isNotEmpty())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("정상적으로 처리되었습니다."))
                .andExpect(jsonPath("$.data.content").isEmpty())
                .andExpect(jsonPath("$.data.number").value("0"))
                .andExpect(jsonPath("$.data.size").value("20"))
                .andDo(print());


        //then
        then(postsService).should().findAll(eq(1L), eq("backend"),
                eq(new String[]{"title", "content"}) ,
                eq(new String[]{"cl", "37.619774", "127.060926"}),
                eq(pageable));
    }
}
