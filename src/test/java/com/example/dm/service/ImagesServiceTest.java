package com.example.dm.service;

import com.example.dm.exception.BadApiRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ImagesServiceTest {

    @InjectMocks
    private ImagesServiceImpl imagesService;

    @DisplayName("이미지 저장할 때 엔티티 리스트가 널이면 예외 발생")
    @Test
    void saveAll_images_null() {
        // expected
        Assertions.assertThatThrownBy(() -> imagesService.saveAll(null))
                .isInstanceOf(BadApiRequestException.class)
                .hasMessage("잘못된 요청입니다. [Image is a required value]");

    }

    @DisplayName("이미지 저장할 때 엔티티 리스트가 비어있이면 예외 발생")
    @Test
    void saveAll_images_empty() {
        // expected
        Assertions.assertThatThrownBy(() -> imagesService.saveAll(List.of()))
                .isInstanceOf(BadApiRequestException.class)
                .hasMessage("잘못된 요청입니다. [Image is a required value]");

    }

    @DisplayName("이미지 삭제할 때 엔티티 리스트가 널이면 예외 발생")
    @Test
    void deleteAll_images_null() {
        // expected
        Assertions.assertThatThrownBy(() -> imagesService.deleteAll(null))
                .isInstanceOf(BadApiRequestException.class)
                .hasMessage("잘못된 요청입니다. [Image is a required value]");

    }

    @DisplayName("이미지 삭제할 때 엔티티 리스트가 비어있이면 예외 발생")
    @Test
    void deleteAll_images_empty() {
        // expected
        Assertions.assertThatThrownBy(() -> imagesService.deleteAll(List.of()))
                .isInstanceOf(BadApiRequestException.class)
                .hasMessage("잘못된 요청입니다. [Image is a required value]");

    }
}