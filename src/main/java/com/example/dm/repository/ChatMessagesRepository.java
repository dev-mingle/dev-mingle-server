package com.example.dm.repository;

import com.example.dm.dto.chats.ChatMessageGetDto;
import com.example.dm.entity.ChatMessages;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessagesRepository extends MongoRepository<ChatMessages, String> {

    List<ChatMessageGetDto> findByRoomId(Long roomId, Sort sort);

}
