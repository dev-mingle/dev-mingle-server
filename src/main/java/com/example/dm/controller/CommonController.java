package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path.default}/commons")
public class CommonController extends BaseController {

    /**
     * 테스트 API
     */
    @GetMapping("/test")
    public ResponseEntity<ApiResponse> test() {
        return responseBuilder(true, HttpStatus.OK);
    }

}
