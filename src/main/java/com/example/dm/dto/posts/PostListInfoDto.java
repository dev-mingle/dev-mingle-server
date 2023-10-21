package com.example.dm.dto.posts;

import com.example.dm.entity.Posts;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostListInfoDto {
    private final Long id;
    private final String nickname;
    private final String category;
    private final String title;
    private final String contents;
    private final int hits;
    private final int likes;
    private final boolean hasChat;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static PostListInfoDto convertFromPosts(Posts posts, String category) {
        return PostListInfoDto.builder()
                .id(posts.getId())
                .nickname(posts.getUserProfile().getNickname())
                .category(category)
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
