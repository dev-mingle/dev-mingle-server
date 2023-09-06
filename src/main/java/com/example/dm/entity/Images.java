package com.example.dm.entity;

import com.example.dm.enums.ImageType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Images extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  String url;
  ImageType type;
  Long reference_id;
  @Builder.Default
  private boolean is_deleted = false;
  private LocalDateTime deleted_at;

  public static Images create(String url, ImageType type, Long reference_id){
    Images images = new Images();
    images.setUrl(url);
    images.setType(type);
    images.setReference_id(reference_id);
    return images;
  }
}
