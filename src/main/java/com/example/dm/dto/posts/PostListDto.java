package com.example.dm.dto.posts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostListDto(@NotNull Long categoryId, String search, String[] conditions,
                          @Size(min = 2, max = 2) double[] location) {
}
