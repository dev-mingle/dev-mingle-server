package com.example.dm.service;

import com.example.dm.entity.Images;
import com.example.dm.enums.ImageType;

import java.util.List;

public interface ImagesService {
    List<Images> findAllByReferenceIdAndType(Long referenceId, ImageType imageType);

    void saveAll(List<Images> imagesList);

    void update(Long referenceId, ImageType imageType, List<Images> imagesList);

    void delete(Long postsId, ImageType posts);
}
