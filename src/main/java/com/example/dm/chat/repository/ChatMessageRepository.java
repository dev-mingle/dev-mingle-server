package com.example.dm.chat.repository;

import com.example.dm.chat.dto.ChatMessageDto;
import com.example.dm.chat.entity.ChatMessage;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessageDto> findByRoomId(Long roomId, Sort sort);

}
