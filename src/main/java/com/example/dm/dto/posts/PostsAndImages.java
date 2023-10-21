package com.example.dm.dto.posts;

import com.example.dm.entity.Images;
import com.example.dm.entity.Posts;

import java.util.List;

public record PostsAndImages(Posts posts, List<Images> imagesList) {
}
