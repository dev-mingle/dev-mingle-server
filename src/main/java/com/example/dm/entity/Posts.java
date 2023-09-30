package com.example.dm.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Posts extends DeletedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfiles userProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String contents;

    private double latitude;
    private double longitude;

    @ColumnDefault("0")
    private int hits;
    @ColumnDefault("0")
    private int likes;
    @ColumnDefault("false")
    @Column(name = "has_chat")
    private boolean hasChat;

    @Builder
    public Posts(Long id, UserProfiles userProfile, Categories category, String title, String contents, double latitude, double longitude, int hits, int likes, boolean hasChat) {
        this.id = id;
        this.userProfile = userProfile;
        this.category = category;
        this.title = title;
        this.contents = contents;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hits = hits;
        this.likes = likes;
        this.hasChat = hasChat;
    }
}
