package com.example.dm.post.entity;

import com.example.dm.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;
//    private UserProfile userProfile;
//    private Category category;
    private String title;
    private String contents;
    private double latitude;
    private double longitude;
    private double displayRadius;
    private int hits;
    private int likes;
    private boolean hasChat;
    private boolean isDeleted;
    private LocalDateTime deletedAt;

    @Builder
    public Posts(LocalDateTime createdAt, LocalDateTime updatedAt, String title, String contents, double latitude, double longitude, double displayRadius, int hits, int likes, boolean hasChat, boolean isDeleted, LocalDateTime deletedAt) {
        super(createdAt, updatedAt);
        this.title = title;
        this.contents = contents;
        this.latitude = latitude;
        this.longitude = longitude;
        this.displayRadius = displayRadius;
        this.hits = hits;
        this.likes = likes;
        this.hasChat = hasChat;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }
}
