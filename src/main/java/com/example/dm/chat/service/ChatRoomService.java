package com.example.dm.chat.service;

import com.example.dm.chat.dto.ChatRoomCreateDto;
import com.example.dm.chat.dto.ChatRoomGetDto;
import com.example.dm.chat.dto.ChatRoomDto;
import com.example.dm.chat.dto.UserProfileDto;
import com.example.dm.chat.entity.ChatRoom;
import com.example.dm.chat.entity.ChatRoomUserProfiles;
import com.example.dm.chat.repository.ChatRoomRepository;
import com.example.dm.entity.UserProfiles;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.BusinessException;
import com.example.dm.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
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

    // todo: 방장 표시
    public List<ChatRoomGetDto> findRoomByUser(Long userProfileId) {
        UserProfiles userProfiles = verifyUserProfile(userProfileId);

        return userProfiles.getChatRooms().stream().map(chatRoomUser -> {
            ChatRoom chatRoom = chatRoomUser.getChatRoom();
            return ChatRoomGetDto.builder()
                    .roomId(chatRoom.getRoomId())
                    .name(chatRoom.getName())
                    .userCount(chatRoom.getUserCount())
                    .build();
        }).collect(Collectors.toList());
    }

    public ChatRoomDto enterRoomUser(Long roomId, Long userProfileId) {
        ChatRoom chatRoom = verifyRoom(roomId);
        UserProfiles userProfiles = verifyUserProfile(userProfileId);

        ChatRoomUserProfiles chatRoomUserProfiles = ChatRoomUserProfiles.builder()
                .userProfiles(userProfiles)
                .chatRoom(chatRoom)
                .build();
        chatRoom.addUser(chatRoomUserProfiles);
        chatRoom.plusUserCount();

        return ChatRoomDto.builder()
                .roomId(chatRoom.getRoomId())
                .roomName(chatRoom.getName())
                .userProfileId(userProfiles.getId())
                .nickname(userProfiles.getNickname())
                .build();
    }

    // todo: 방장이 퇴장할 경우
    // todo: nickname 주석 authentication으로 처리
    public ChatRoomDto exitRoomUser(Long roomId, Long userProfileId) {
        ChatRoom chatRoom = verifyRoom(roomId);

        if(!chatRoom.removeUser(userProfileId)) throw new BusinessException(ApiResultStatus.USER_NOT_EXIST_ROOM);
        chatRoom.minusUserCount();

        chatRoomRepository.save(chatRoom);

        return ChatRoomDto.builder()
                .roomId(chatRoom.getRoomId())
                .roomName(chatRoom.getName())
                .userProfileId(userProfileId)
//                .nickname(userProfiles.getNickname())
                .build();
    }

    public List<UserProfileDto> getRoomUserList(Long roomId) {
        ChatRoom chatRoom = verifyRoom(roomId);

        return chatRoom.getUserProfiles().stream()
                .map(chatRoomUserProfiles -> UserProfileDto.from(chatRoomUserProfiles.getUserProfiles()))
                .collect(Collectors.toList());
    }

    private ChatRoom verifyRoom(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(() -> new BusinessException(ApiResultStatus.ROOM_NOT_FOUND));
    }

    private UserProfiles verifyUserProfile(Long userProfileId) {
        return userProfileRepository.findById(userProfileId).orElseThrow(() -> new BusinessException(ApiResultStatus.USER_NOT_FOUND));
    }
}
