package com.example.dm.service;

import com.example.dm.entity.Images;
import com.example.dm.enums.ImageType;
import com.example.dm.exception.BadApiRequestException;
import com.example.dm.repository.ImagesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ImagesServiceTest {

    @InjectMocks
    private ImagesServiceImpl imagesService;
    @Mock
    private ImagesRepository imagesRepository;
    @Captor
    private ArgumentCaptor<List<Images>> imagesCaptor;

    @DisplayName("이미지 저장할 때 엔티티 리스트가 널이면 예외 발생")
    @Test
    void saveAll_images_null() {
        // expected
        assertThatThrownBy(() -> imagesService.saveAll(null))
                .isInstanceOf(BadApiRequestException.class)
                .hasMessage("잘못된 요청입니다. [Image is a required value]");

    }

    @DisplayName("이미지 저장할 때 엔티티 리스트가 비어있이면 예외 발생")
    @Test
    void saveAll_images_empty() {
        // expected
        assertThatThrownBy(() -> imagesService.saveAll(List.of()))
                .isInstanceOf(BadApiRequestException.class)
                .hasMessage("잘못된 요청입니다. [Image is a required value]");

    }

    @DisplayName("업데이트 이미지 목록과 조회 이미지 목록이 일치한다면 삽입과 삭제가 일어나지 않음")
    @Test
    void update_nothing_happens() {
        // given
        List<Images> images = getImages(5, true);
        given(imagesRepository.findAllByReferenceIdAndTypeAndIsDeletedFalse(1L, ImageType.Posts)).willReturn(images);

        // when
        imagesService.update(1L, ImageType.Posts, images);

        // then
        then(imagesRepository).should().findAllByReferenceIdAndTypeAndIsDeletedFalse(1L, ImageType.Posts);
        then(imagesRepository).shouldHaveNoMoreInteractions();
        assertThat(images.stream().noneMatch(Images::isDeleted)).isTrue();
    }

    @DisplayName("업데이트 이미지 목록에 id가 없는 이미지는 저장")
    @Test
    void update_images_save_if_id_null() {
        // given
        List<Images> toUpdateImages = getImages(2, false);
        given(imagesRepository.findAllByReferenceIdAndTypeAndIsDeletedFalse(1L, ImageType.Posts)).willReturn(List.of());

        // when
        imagesService.update(1L, ImageType.Posts, toUpdateImages);

        // then
        then(imagesRepository).should().saveAll(imagesCaptor.capture());
        assertThat(imagesCaptor.getValue().stream().map(Images::getUrl)).containsExactly("images0false", "images1false");
    }

    @DisplayName("업데이트 이미지 목록에 존재하지 않지만, 조회 이미지 목록에 존재한다면 delete 속성 true 변경")
    @Test
    void update_delete_attribute_true() {
        // given
        List<Images> findImages = getImages(5, true);
        given(imagesRepository.findAllByReferenceIdAndTypeAndIsDeletedFalse(1L, ImageType.Posts)).willReturn(findImages);

        // when
        imagesService.update(1L, ImageType.Posts, null);

        // then
        then(imagesRepository).should().findAllByReferenceIdAndTypeAndIsDeletedFalse(1L, ImageType.Posts);
        then(imagesRepository).shouldHaveNoMoreInteractions();
        assertThat(findImages.stream().allMatch(Images::isDeleted)).isTrue();
    }

    @DisplayName("아이디가 없는 이미지는 저장하고, 조회 목록에만 존재하는 이미지는 삭제")
    @Test
    void update_image_save_and_delete_true() {
        // given
        List<Images> findImages = getImages(5, true);
        List<Images> toUpdateImages = new ArrayList<>(findImages.subList(0, 3));
        toUpdateImages.addAll(getImages(3, false));
        given(imagesRepository.findAllByReferenceIdAndTypeAndIsDeletedFalse(1L, ImageType.Posts)).willReturn(findImages);

        // when
        imagesService.update(1L, ImageType.Posts, toUpdateImages);

        // then
        then(imagesRepository).should().saveAll(imagesCaptor.capture());
        assertThat(imagesCaptor.getValue()
                .stream()
                .map(Images::getUrl)
        ).containsExactly("images0false", "images1false", "images2false");
        assertThat(findImages.stream()
                .filter(Images::isDeleted)
                .map(Images::getId)
        ).containsExactly(3L, 4L);
    }

    @DisplayName("이미지 삭제는 이미지 업데이트 delete = true 호출")
    @Test
    void delete() {
        // given
        List<Images> images = getImages(5, true);
        given(imagesRepository.findAllByReferenceIdAndTypeAndIsDeletedFalse(1L, ImageType.Posts))
                .willReturn(images);

        // when
        imagesService.delete(1L, ImageType.Posts);

        // then
        assertThat(images.stream().allMatch(Images::isDeleted)).isTrue();
    }

    private static List<Images> getImages(int n, boolean hasId) {
        return LongStream.range(0, n)
                .mapToObj(i ->{
                    Images.ImagesBuilder builder = Images.builder();
                    if (hasId) {
                        builder.id(i);
                    }
                    return builder
                            .url("images" + i + hasId)
                            .type(ImageType.Posts)
                            .referenceId(1L)
                            .build();
                }).toList();
    }
}