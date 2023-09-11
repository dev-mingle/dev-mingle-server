package com.example.dm.chat.service;

import com.example.dm.chat.dto.ChatDto;
import com.example.dm.chat.dto.ChatMessageDto;
import com.example.dm.chat.entity.ChatMessage;
import com.example.dm.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    // todo: room별 저장?
    public List<ChatMessageDto> findAllMessageByRoomId(Long roomId) {
        return chatMessageRepository.findByRoomId(roomId, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public void saveMessage(ChatDto chatDto) {
        ChatMessage chatMessage = ChatMessage.builder()
                .message(chatDto.getMessage())
                .sender(chatDto.getSender())
                .roomId(chatDto.getRoomId())
                .build();

        chatMessageRepository.save(chatMessage);
    }
}
