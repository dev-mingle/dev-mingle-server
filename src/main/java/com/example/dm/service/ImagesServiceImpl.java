package com.example.dm.service;

import com.example.dm.entity.Images;
import com.example.dm.enums.ImageType;
import com.example.dm.exception.BadApiRequestException;
import com.example.dm.repository.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
@RequiredArgsConstructor
public class ImagesServiceImpl implements ImagesService{
    private final ImagesRepository imagesRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Images> findAllByReferenceIdAndType(Long referenceId, ImageType imageType) {
        return imagesRepository.findAllByReferenceIdAndTypeAndIsDeletedFalse(referenceId, imageType);
    }

    @Override
    public void saveAll(List<Images> imagesList) {
        if (imagesList == null || imagesList.isEmpty()) {
            throw new BadApiRequestException("Image is a required value");
        }
        imagesRepository.saveAll(imagesList);
    }

    @Override
    public void update(Long referenceId, ImageType imageType, List<Images> imagesList) {
        List<Images> findImages = imagesRepository.findAllByReferenceIdAndTypeAndIsDeletedFalse(referenceId, imageType);
        List<Images> toSaveImage = new ArrayList<>();
        Set<Long> savedImageId = new HashSet<>();

        if (imagesList != null) {
            for (Images images : imagesList) {
                Long imagesId = images.getId();
                if (imagesId == null) {
                    toSaveImage.add(images);
                } else {
                    savedImageId.add(imagesId);
                }
            }
        }

        if (!toSaveImage.isEmpty()) {
            saveAll(toSaveImage);
        }

        findImages.stream()
                .filter(img -> !savedImageId.contains(img.getId()))
                .forEach(Images::delete);
    }

    @Override
    public void delete(Long postsId, ImageType posts) {
        update(postsId, posts, null);
    }
}
