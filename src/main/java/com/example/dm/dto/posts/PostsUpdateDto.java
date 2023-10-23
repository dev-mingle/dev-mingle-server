package com.example.dm.dto.posts;

import com.example.dm.dto.images.ImagesDto;
import com.example.dm.entity.Categories;
import com.example.dm.entity.Images;
import com.example.dm.entity.Posts;
import com.example.dm.enums.ImageType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostsUpdateDto {
    @NotBlank
    private final String category;
    @NotBlank
    private final String title;
    @NotBlank
    private final String contents;
    private final boolean hasChat;
    @Valid
    private final List<ImagesDto> images;

    public Posts convertPosts(Categories categories) {
        return Posts.builder()
                .category(categories)
                .title(title)
                .contents(contents)
                .hasChat(hasChat)
                .build();
    }

    public List<Images> getImages(Long postsId) {
        if (images == null) {
            return null;
        }

        return images.stream()
                .map(dto -> dto.convertToImages(ImageType.Posts, postsId))
                .toList();
    }
}
