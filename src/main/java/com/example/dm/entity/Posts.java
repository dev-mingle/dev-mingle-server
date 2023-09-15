package com.example.dm.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

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
    private Point point;
    private int hits;
    private int likes;
    private boolean hasChat;

    @Builder
    public Posts(Long id, UserProfiles userProfile, Categories category, String title,
                 String contents, Point point, int hits, int likes, boolean hasChat) {
        this.id = id;
        this.userProfile = userProfile;
        this.category = category;
        this.title = title;
        this.contents = contents;
        this.point = point;
        this.hits = hits;
        this.likes = likes;
        this.hasChat = hasChat;
    }
}
