package com.example.dm.chat.service;

import com.example.dm.chat.dto.ChatCreateDto;
import com.example.dm.chat.dto.ChatMessageGetDto;
import com.example.dm.chat.entity.ChatMessages;
import com.example.dm.chat.repository.ChatMessagesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

    private final ChatMessagesRepository chatMessagesRepository;

    // todo: room별 저장?
    public List<ChatMessageGetDto> findAllMessageByRoomId(Long roomId) {
        return chatMessagesRepository.findByRoomId(roomId, Sort.by(Sort.Direction.ASC, "createdAt"));
    }

    public ChatMessageGetDto saveMessage(ChatCreateDto chatCreateDto) {
        ChatMessages chatMessages = ChatMessages.from(chatCreateDto);

        ChatMessages message = chatMessagesRepository.save(chatMessages);
        return ChatMessageGetDto.from(message);
    }
}
