package com.example.dm.repository;

import com.example.dm.entity.Images;
import com.example.dm.enums.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImagesRepository extends JpaRepository<Images, Long> {

    @Query("select i from Images i where i.referenceId = :referenceId and i.type = :type")
    List<Images> findByReferenceIdAndType(Long referenceId, ImageType type);
}
