package com.example.dm.service;

import com.example.dm.entity.Images;
import com.example.dm.enums.ImageType;
import com.example.dm.exception.BadApiRequestException;
import com.example.dm.repository.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ImagesServiceImpl implements ImagesService{
    private final ImagesRepository imagesRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Images> findByReferenceId(Long referenceId, ImageType imageType) {
        return imagesRepository.findByReferenceIdAndType(referenceId, imageType);
    }

    @Override
    public void saveAll(List<Images> imagesList) {
        if (imagesList == null || imagesList.isEmpty()) {
            throw new BadApiRequestException("Image is a required value");
        }
        imagesRepository.saveAll(imagesList);
    }

    @Override
    public void deleteAll(List<Images> imagesList) {
        if (imagesList == null || imagesList.isEmpty()) {
            throw new BadApiRequestException("Image is a required value");
        }
        imagesRepository.deleteAll(imagesList);
    }
}
