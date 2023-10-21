package com.example.dm.dto.posts;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PostListDto(@NotBlank String category,
                          String search,
                          List<String> conditions,
                          @NotNull @Min(0) Double latitude,
                          @NotNull @Min(0) Double longitude) {
}
