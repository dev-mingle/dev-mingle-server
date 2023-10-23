package com.example.dm.dto.posts;

import com.example.dm.entity.Categories;
import com.example.dm.entity.Posts;
import com.example.dm.entity.UserProfiles;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostSaveDto {

    @NotBlank
    private final String category;
    @NotBlank
    private final String title;
    @NotBlank
    private final String contents;
    @Min(0)
    @NotNull
    private final Double latitude;
    @Min(0)
    @NotNull
    private final Double longitude;
    private final boolean hasChat;
    private final List<String> imageUrl;

    public Posts convertToPosts(UserProfiles userProfiles, Categories categories) {
        return Posts.builder()
                .userProfile(userProfiles)
                .category(categories)
                .title(title)
                .contents(contents)
                .latitude(latitude)
                .longitude(longitude)
                .hasChat(hasChat)
                .build();
    }
}
