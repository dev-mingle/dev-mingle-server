package com.example.dm.dto.posts;

import com.example.dm.dto.images.ImagesDto;
import com.example.dm.entity.Categories;
import com.example.dm.entity.Posts;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Builder
public class PostsUpdateDto {
    @NotNull
    private Long categoryId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String contents;
    private boolean hasChat;
    @Valid
    private List<ImagesDto> images;

    public Posts convertPosts() {
        return Posts.builder()
                .category(Categories.builder().id(categoryId).build())
                .title(title)
                .contents(contents)
                .hasChat(hasChat)
                .build();
    }
}
