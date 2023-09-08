package com.example.dm.post.entity;

import com.example.dm.category.entity.Categories;
import com.example.dm.entity.DeletedEntity;
import com.example.dm.entity.UserProfiles;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Posts extends DeletedEntity {

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

    @Builder
    public Posts(Long id, UserProfiles userProfile, Categories category, String title, String contents,
                 double latitude, double longitude, double displayRadius, int hits, int likes, boolean hasChat) {
        this.id = id;
        this.userProfile = userProfile;
        this.category = category;
        this.title = title;
        this.contents = contents;
        this.latitude = latitude;
        this.longitude = longitude;
        this.displayRadius = displayRadius;
        this.hits = hits;
        this.likes = likes;
        this.hasChat = hasChat;
    }
}
