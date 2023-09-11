package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.dto.follows.FollowAddDto;
import com.example.dm.dto.follows.FollowInfoDto;
import com.example.dm.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path.default}/follows")
public class FollowController extends BaseController {

    private final FollowService followService;

    /**
     * 팔로우 등록 API
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse> addFollow(@RequestBody FollowAddDto inputDto) {
        FollowInfoDto followInfoDto = followService.addFollows(inputDto, getCurrentUserProfiles());
        return responseBuilder(followInfoDto, HttpStatus.OK);
    }

}
