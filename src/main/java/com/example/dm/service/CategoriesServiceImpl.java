package com.example.dm.service;

import com.example.dm.entity.Categories;
import com.example.dm.exception.BadApiRequestException;
import com.example.dm.repository.CategoriesRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Transactional
@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService{

    private final CategoriesRepository categoriesRepository;

    private final Map<Long, Categories> categoriesMap = new ConcurrentHashMap<>();
    private final Map<String, Long> nameIdMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        categoriesMap.put(-1L, Categories.builder().id(-1L).name("All").build());
    }

    @Transactional(readOnly = true)
    @Override
    public Categories getCategories(Long categoryId) {
        categoryId = Long.max(categoryId, -1L);

        if (!categoriesMap.containsKey(categoryId)) {
            Categories categories = categoriesRepository.findById(categoryId)
                    .orElseThrow(() -> new BadApiRequestException("카테고리를 찾을 수 없습니다."));
            categoriesMap.put(categoryId, categories);
            nameIdMap.put(categories.getName(), categoryId);
        }

        return categoriesMap.get(categoryId);
    }

    @Transactional(readOnly = true)
    @Override
    public Categories getCategories(String category) {
        if (!nameIdMap.containsKey(category)) {
            Categories categories = categoriesRepository.findByName(category)
                    .orElseThrow(() -> new BadApiRequestException("카테고리를 찾을 수 없습니다."));
            Long categoryId = categories.getId();
            categoriesMap.put(categoryId, categories);
            nameIdMap.put(category, categoryId);
        }

        Long categoryId = nameIdMap.get(category);
        return categoriesMap.get(categoryId);
    }

    @Override
    public Long save(Categories categories) {
        categoriesTableClear();
        return categoriesRepository.save(categories).getId();
    }

    @Override
    public Long delete(Categories categories) {
        categoriesTableClear();
        categoriesRepository.delete(categories);
        return categories.getId();
    }

    private void categoriesTableClear() {
        categoriesMap.clear();
        nameIdMap.clear();
    }
}
