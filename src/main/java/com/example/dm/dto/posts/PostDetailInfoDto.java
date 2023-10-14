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
    private Long id;
    private Long userProfileId;
    private Long categoryId;
    private String title;
    private String contents;
    private double latitude;
    private double longitude;
    private int hits;
    private int likes;
    private boolean hasChat;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ImagesDto> images;

    public static PostDetailInfoDto convertPosts(Posts posts, List<Images> images) {
        List<ImagesDto> imagesDtoList = images.stream()
                .map(ImagesDto::convertFromImages)
                .toList();

        return PostDetailInfoDto.builder()
                .id(posts.getId())
                .userProfileId(posts.getUserProfile().getId())
                .categoryId(posts.getCategory().getId())
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
