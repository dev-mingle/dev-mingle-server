package com.example.dm.dto.posts;

import com.example.dm.dto.images.ImagesDto;
import com.example.dm.entity.Images;
import com.example.dm.entity.Posts;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostDetailInfoDto {
    private final Long id;
    private final String nickname;
    private final String category;
    private final String title;
    private final String contents;
    private final double latitude;
    private final double longitude;
    private final int hits;
    private final int likes;
    private final boolean hasChat;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<ImagesDto> images;

    public static PostDetailInfoDto convertFromPostsAndImages(PostsAndImages postsAndImages, String category) {
        Posts posts = postsAndImages.posts();
        List<ImagesDto> imagesDtoList = postsAndImages.imagesList()
                .stream()
                .map(ImagesDto::convertFromImages)
                .toList();

        return PostDetailInfoDto.builder()
                .id(posts.getId())
                .nickname(posts.getUserProfile().getNickname())
                .category(category)
                .title(posts.getTitle())
                .contents(posts.getContents())
                .latitude(posts.getLatitude())
                .longitude(posts.getLongitude())
                .hits(posts.getHits())
                .likes(posts.getLikes())
                .hasChat(posts.isHasChat())
                .createdAt(posts.getCreatedAt())
                .updatedAt(posts.getUpdatedAt())
                .images(imagesDtoList)
                .build();
    }
}
