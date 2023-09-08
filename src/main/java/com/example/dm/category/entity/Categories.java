package com.example.dm.category.entity;

import com.example.dm.entity.DeletedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    private String creator;

    @Builder
    public Categories(Long id, String name, String creator) {
        this.id = id;
        this.name = name;
        this.creator = creator;
    }
}
