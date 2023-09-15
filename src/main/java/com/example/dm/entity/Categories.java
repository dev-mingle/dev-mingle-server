package com.example.dm.entity;

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
public class Categories extends DeletedEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private UserProfiles creator;

    @Builder
    public Categories(Long id, String name, UserProfiles creator) {
        this.id = id;
        this.name = name;
        this.creator = creator;
    }
}
