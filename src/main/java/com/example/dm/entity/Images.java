package com.example.dm.entity;

import com.example.dm.enums.ImageType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "images")
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
public class Images extends DeletedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @Enumerated(EnumType.STRING)
    private ImageType type;
    private Long referenceId;

    @Builder
    public Images(Long id, String url, ImageType type, Long referenceId) {
        this.id = id;
        this.url = url;
        this.type = type;
        this.referenceId = referenceId;
    }

    public static Images create(String url, ImageType type, Long referenceId) {
        Images images = new Images();
        images.setUrl(url);
        images.setType(type);
        images.setReferenceId(referenceId);
        return images;
    }
}
