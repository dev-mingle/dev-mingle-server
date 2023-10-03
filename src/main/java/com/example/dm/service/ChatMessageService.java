package com.example.dm.service;

import com.example.dm.dto.chats.ChatCreateDto;
import com.example.dm.dto.chats.ChatMessageGetDto;
import com.example.dm.entity.ChatMessages;
import com.example.dm.entity.ChatRooms;
import com.example.dm.entity.LoginUser;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.BusinessException;
import com.example.dm.repository.ChatMessagesRepository;
import com.example.dm.repository.ChatRoomsRepository;
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

    private final ChatRoomsRepository chatRoomsRepository;

    // todo: room별 저장?
    public List<ChatMessageGetDto> findAllMessageByRoomId(Long roomId, LoginUser user) {
        ChatRooms chatRooms = verifyRoom(roomId);

        if(!verifyJoinUser(user, chatRooms)) throw new BusinessException(ApiResultStatus.USER_NOT_EXIST_ROOM);

        return chatMessagesRepository.findByRoomId(roomId, Sort.by(Sort.Direction.ASC, "createdAt"));
    }

    public ChatMessageGetDto saveMessage(ChatCreateDto chatCreateDto,
                                         Long roomId,
                                         LoginUser user) {
        ChatMessages chatMessages = ChatMessages.from(chatCreateDto, roomId, user);

        ChatMessages message = chatMessagesRepository.save(chatMessages);
        return ChatMessageGetDto.from(message);
    }

    public ChatMessageGetDto updateMessage(ChatPatchDto chatPatchDto, LoginUser user) {
        ChatMessages chatMessages = chatMessagesRepository.findById(chatPatchDto.getMessageId()).orElseThrow(() -> new BusinessException(ApiResultStatus.MESSAGE_NOT_EXIST));
        if(!chatMessages.getUserProfileId().equals(user.getId())) throw new BusinessException(ApiResultStatus.FORBIDDEN);

        ChatMessages correctMessages = chatMessages.updateMessage(chatPatchDto);
        ChatMessages savedMessages = chatMessagesRepository.save(correctMessages);
        return ChatMessageGetDto.from(savedMessages);
    }

    private ChatRooms verifyRoom(Long roomId) {
        return chatRoomsRepository.findById(roomId).orElseThrow(() -> new BusinessException(ApiResultStatus.ROOM_NOT_FOUND));
    }

    private boolean verifyJoinUser(LoginUser user, ChatRooms chatRooms) {
        return chatRooms.getChatMembers().stream().anyMatch(members -> members.getUserProfiles().getId().equals(user.getId()));
    }
}
