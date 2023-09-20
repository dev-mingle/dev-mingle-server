package com.example.dm.chat.repository;

import com.example.dm.chat.dto.ChatMessageDto;
import com.example.dm.chat.entity.ChatMessages;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessages, String> {

    List<ChatMessageDto> findByRoomId(Long roomId, Sort sort);

}
