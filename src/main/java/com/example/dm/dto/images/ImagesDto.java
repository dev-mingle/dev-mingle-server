package com.example.dm.dto.images;

import com.example.dm.entity.Images;
import com.example.dm.enums.ImageType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ImagesDto {
    private final Long id;
    @NotEmpty
    private final String url;

    public Images convertToImages(ImageType type, Long referenceId) {
        return Images.builder()
                .id(id)
                .url(url)
                .type(type)
                .referenceId(referenceId)
                .build();
    }

    public static ImagesDto convertFromImages(Images images) {
        return ImagesDto.builder()
                .id(images.getId())
                .url(images.getUrl())
                .build();
    }
}
