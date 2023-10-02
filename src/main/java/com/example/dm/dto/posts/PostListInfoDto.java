package com.example.dm.dto.posts;

import com.example.dm.entity.Posts;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostListInfoDto {

    private Long id;
    private Long userProfileId;
    private Long categoryId;
    private String title;
    private String contents;
    private int hits;
    private int likes;
    private boolean hasChat;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostListInfoDto convertPosts(Posts posts) {
        return PostListInfoDto.builder()
                .id(posts.getId())
                .userProfileId(posts.getUserProfile().getId())
                .categoryId(posts.getCategory().getId())
                .title(posts.getTitle())
                .contents(posts.getContents())
                .hits(posts.getHits())
                .likes(posts.getLikes())
                .hasChat(posts.isHasChat())
                .createdAt(posts.getCreatedAt())
                .updatedAt(posts.getUpdatedAt())
                .build();
    }
}
