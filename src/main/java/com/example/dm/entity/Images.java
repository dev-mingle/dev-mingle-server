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

  String url;
  ImageType type;
  Long reference_id;
  @ColumnDefault("false")
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
