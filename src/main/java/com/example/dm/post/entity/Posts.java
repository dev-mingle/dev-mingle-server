package com.example.dm.post.entity;

import com.example.dm.category.entity.Categories;
import com.example.dm.entity.BaseTimeEntity;
import com.example.dm.entity.UserProfiles;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
    private UserProfiles userProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Categories category;

    private String title;
    private String contents;
    private double latitude;
    private double longitude;
    private double displayRadius;
    private int hits;
    private int likes;
    private boolean hasChat;
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "deleted_at")
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
