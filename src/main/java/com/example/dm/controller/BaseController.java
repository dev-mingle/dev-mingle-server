package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.entity.UserProfiles;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.repository.UserProfileRepository;
import com.example.dm.util.TxidGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    @Autowired
    private TxidGenerator txidGenerator;

    @Autowired
    private UserProfileRepository userProfileRepository;

    protected ResponseEntity<ApiResponse> responseBuilder(Object data, HttpStatus httpStatus) {
        ApiResponse apiResponse = ApiResponse.SetSuccessStatus.builder()
                .txid(txidGenerator.getTxid())
                .status(httpStatus)
                .message(ApiResultStatus.REQUEST_SUCCESS.getMessage())
                .data(data)
                .build();

        return new ResponseEntity<>(apiResponse, httpStatus);
    }

    /**
     * 유저 인증 정보 가져오기
     */
//    private UserAuthDto getAuthInfo() {
//        return (UserAuthDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    }

    /**
     * 현재 UserId 가져오기
     * 추후 유저 인증 정보로부터 파싱할 수 있도록 대체 필요
     */
    protected Long getUserId() {
//        return this.getAuthInfo().getUserId();
        return 1L;
    }

    /**
     * 현재 유저 정보 조회
     */
//    protected Users getCurrentUsers() {
//        return userRepository.findByIdAndIsDeletedIsFalse(this.getUserId())
//                .orElseThrow(() -> new EntityNotFoundException(ApiResultStatus.USER_NOT_FOUND.getMessage()));
//    }

    /**
     * 현재 유저 프로필 정보 조회
     */
    protected UserProfiles getCurrentUserProfiles() {
        return userProfileRepository.findByUsers_IdAndIsDeletedIsFalse(this.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(ApiResultStatus.USER_NOT_FOUND.getMessage()));
    }
}
