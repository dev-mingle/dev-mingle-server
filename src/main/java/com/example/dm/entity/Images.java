package com.example.dm.entity;

import com.example.dm.enums.ImageType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "images")
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Images extends DeletedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String url;
    @Enumerated(EnumType.STRING)
    private ImageType type;
    private Long referenceId;

    public static Images create(String url, ImageType type, Long referenceId) {
        Images images = new Images();
        images.setUrl(url);
        images.setType(type);
        images.setReferenceId(referenceId);
        return images;
    }
}
