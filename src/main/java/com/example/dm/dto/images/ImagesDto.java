package com.example.dm.dto.images;

import com.example.dm.entity.Images;
import com.example.dm.enums.ImageType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ImagesDto {
    private final Long id;
    @NotEmpty
    private final String url;
    @NotNull
    private final ImageType type;
    @NotNull
    @Min(0)
    private final Long referenceId;

    public Images convertToImages() {
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
                .type(images.getType())
                .referenceId(images.getReferenceId())
                .build();
    }
}
