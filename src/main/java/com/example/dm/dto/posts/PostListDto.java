package com.example.dm.dto.posts;

import jakarta.validation.constraints.Size;

public record PostListDto(String search, String[] conditions, @Size(min = 2, max = 2) double[] location) {
}
