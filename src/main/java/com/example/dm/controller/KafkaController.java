package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.dto.commons.KafkaDto;
import com.example.dm.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path.default}/kafka")
public class KafkaController extends BaseController {

    private final KafkaProducer kafkaProducer;

    /**
     * 특정 토픽으로 메시지 발행 테스트
     */
    @PostMapping("/test")
    public ResponseEntity<ApiResponse> produceTest(@RequestBody KafkaDto.InputDto inputDto) {
        kafkaProducer.sendMessage(inputDto);
        return responseBuilder(new KafkaDto.OutputDto(inputDto), HttpStatus.OK);
    }

}
