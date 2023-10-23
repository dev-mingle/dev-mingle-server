package com.example.dm.repository;

import com.example.dm.entity.Images;
import com.example.dm.enums.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagesRepository extends JpaRepository<Images, Long> {

    List<Images> findAllByReferenceIdAndTypeAndIsDeletedFalse(Long referenceId, ImageType type);
}
