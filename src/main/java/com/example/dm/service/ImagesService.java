package com.example.dm.service;

import com.example.dm.entity.Images;
import com.example.dm.enums.ImageType;

import java.util.List;

public interface ImagesService {
    List<Images> findByReferenceId(Long referenceId, ImageType imageType);

    void saveAll(List<Images> imagesList);

    void deleteAll(Long referenceId, ImageType imageType);
}
