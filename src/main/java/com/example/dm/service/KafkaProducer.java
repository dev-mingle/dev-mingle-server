package com.example.dm.service;

import com.example.dm.dto.commons.KafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 특정 토픽으로 메시지 발행
     *
     * @param inputDto
     */
    public void sendMessage(KafkaDto.InputDto inputDto) {
        // 비동기로 메시지 발행
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(inputDto.getTopic(), inputDto.getMessage()).completable();

        future.whenComplete((result, ex) -> {
            if (ex == null) { // 발행 성공
                log.info("[Produce Success] topic={}, message={}, offset={}", inputDto.getTopic(), inputDto.getMessage(), result.getRecordMetadata().offset());

            } else { // 발행 실패
                log.info("[Produce Failed] topic={}, message={}, description={}", inputDto.getTopic(), inputDto.getMessage(), ex.getMessage());
            }
        });
    }


}
