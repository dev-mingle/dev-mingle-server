package com.example.dm.chat.service;

import com.example.dm.chat.dto.ChatRoomDto;
import com.example.dm.chat.entity.ChatRoom;
import com.example.dm.chat.repository.ChatRoomRepository;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserProfileRepository userProfileRepository;

    public ChatRoomGetDto createRoom(ChatRoomCreateDto chatRoomCreateDto) {
        UserProfiles userProfiles = verifyUserProfile(chatRoomCreateDto.getAdminUserId());

        ChatRoom room = ChatRoom.builder()
                .name(chatRoomCreateDto.getName())
                .adminUser(userProfiles)
                .userCount(1)
                .build();

        ChatRoomUserProfiles chatRoomUserProfiles = ChatRoomUserProfiles.builder()
                .userProfiles(userProfiles)
                .chatRoom(room)
                .build();

        room.addUser(chatRoomUserProfiles);

        ChatRoom chatRoom = chatRoomRepository.save(room);

        return ChatRoomGetDto.builder()
                .roomId(chatRoom.getRoomId())
                .name(chatRoom.getName())
                .userCount(chatRoom.getUserCount())
                .build();
    }

    public ChatRoom findRoomById(Long roomId) {
        return verifyRoom(roomId);
    }

    private ChatRoom verifyRoom(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(() -> new BusinessException(ApiResultStatus.ROOM_NOT_FOUND));
    }

    private UserProfiles verifyUserProfile(Long userProfileId) {
        return userProfileRepository.findById(userProfileId).orElseThrow(() -> new BusinessException(ApiResultStatus.USER_NOT_FOUND));
    }
}
