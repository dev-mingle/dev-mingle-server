package com.example.dm.chat.service;

import com.example.dm.chat.dto.ChatDto;
import com.example.dm.chat.dto.ChatMessageDto;
import com.example.dm.chat.entity.ChatMessages;
import com.example.dm.chat.repository.ChatMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    // todo: room별 저장?
    public List<ChatMessageDto> findAllMessageByRoomId(Long roomId) {
        return chatMessageRepository.findByRoomId(roomId, Sort.by(Sort.Direction.ASC, "createdAt"));
    }

    public void saveMessage(ChatDto chatDto) {
        ChatMessages chatMessages = ChatMessages.builder()
                .message(chatDto.getMessage())
                .sender(chatDto.getSender())
                .roomId(chatDto.getRoomId())
                .build();

        chatMessageRepository.save(chatMessages);
    }
}
