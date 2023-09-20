package com.example.dm.chat.repository;

import com.example.dm.chat.dto.ChatMessageGetDto;
import com.example.dm.chat.entity.ChatMessages;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessagesRepository extends MongoRepository<ChatMessages, String> {

    List<ChatMessageGetDto> findByRoomId(Long roomId, Sort sort);

}
