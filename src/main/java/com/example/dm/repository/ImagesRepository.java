package com.example.dm.repository;

import com.example.dm.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImagesRepository extends JpaRepository<Images, Long> {

    @Query("select i from Images i where i.id = :postsId and i.type = com.example.dm.enums.ImageType.Posts")
    List<Images> findPostsImages(Long postsId);
}
