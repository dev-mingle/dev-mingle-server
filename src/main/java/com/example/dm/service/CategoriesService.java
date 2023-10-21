package com.example.dm.service;

import com.example.dm.entity.Categories;

public interface CategoriesService {

    Categories getCategories(Long categoryId);

    Categories getCategories(String category);

    Long save(Categories categories);

    Long delete(Categories categories);
}
