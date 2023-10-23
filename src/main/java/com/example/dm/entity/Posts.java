package com.example.dm.entity;

import com.example.dm.exception.BadApiRequestException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

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

    @Version
    @ColumnDefault("0")
    private Long version;

    @Builder
    public Posts(boolean isDeleted, LocalDateTime deletedAt, Long id, UserProfiles userProfile, Categories category, String title, String contents, double latitude, double longitude, int hits, int likes, boolean hasChat) {
        super(isDeleted, deletedAt);
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


    public void change(Posts posts) {
        validCheck(posts);
        category = posts.getCategory();
        title = posts.getTitle();
        contents = posts.getContents();
        hasChat = posts.isHasChat();
    }

    private void validCheck(Posts posts) {
        if (hasChat && !posts.isHasChat()) {
            throw new BadApiRequestException("채팅방 삭제는 불가능합니다.");
        }
    }
}
