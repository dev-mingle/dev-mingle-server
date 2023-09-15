package com.example.dm.service;

import com.example.dm.entity.Posts;
import com.example.dm.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;

    @Override
    public Page<Posts> findAll(Long categoryId, String search, String[] conditions, String[] location, Pageable pageable) {
        if (location[0].equals("pl")) {
            /*
             * 유저 프로필에서 위도 경도 찾는 로직
             * UserProfiles userProfiles = userProfilesService.findById(id);
             * location[1] = String.valueOf(userProfiles.getLatitude());
             * location[2] = String.valueOf(userProfiles.getLongitude());
             */
        }

        return postsRepository.findAll(categoryId, search, conditions, location, pageable);
    }
}
