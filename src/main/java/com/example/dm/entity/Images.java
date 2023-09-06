package com.example.dm.entity;

import com.example.dm.enums.ImageType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Images extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String url;
  private ImageType type;
  private Long referenceId;
  @ColumnDefault("false")
  private boolean isDeleted = false;
  private LocalDateTime deletedAt;

  public static Images create(String url, ImageType type, Long referenceId){
    Images images = new Images();
    images.setUrl(url);
    images.setType(type);
    images.setReferenceId(referenceId);
    return images;
  }
}
