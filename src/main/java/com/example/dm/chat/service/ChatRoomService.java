package com.example.dm.chat.service;

import com.example.dm.chat.dto.ChatRoomCreateDto;
import com.example.dm.chat.dto.ChatRoomGetDto;
import com.example.dm.chat.dto.ChatRoomDto;
import com.example.dm.chat.dto.UserProfileDto;
import com.example.dm.chat.entity.ChatMembers;
import com.example.dm.chat.entity.ChatRooms;
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

        ChatRooms room = ChatRooms.builder()
                .name(chatRoomCreateDto.getName())
                .adminUser(userProfiles)
                .userCount(1)
                .build();

        ChatMembers chatMembers = ChatMembers.builder()
                .userProfiles(userProfiles)
                .chatRooms(room)
                .build();

        room.addUser(chatMembers);

        ChatRooms chatRooms = chatRoomRepository.save(room);

        return ChatRoomGetDto.builder()
                .roomId(chatRooms.getRoomId())
                .name(chatRooms.getName())
                .userCount(chatRooms.getUserCount())
                .build();
    }

    public ChatRooms findRoomById(Long roomId) {
        return verifyRoom(roomId);
    }

    // todo: 방장 표시
    public List<ChatRoomGetDto> findRoomByUser(Long userProfileId) {
        UserProfiles userProfiles = verifyUserProfile(userProfileId);

        return userProfiles.getChatMembers().stream().map(chatRoomUser -> {
            ChatRooms chatRooms = chatRoomUser.getChatRooms();
            return ChatRoomGetDto.builder()
                    .roomId(chatRooms.getRoomId())
                    .name(chatRooms.getName())
                    .userCount(chatRooms.getUserCount())
                    .build();
        }).collect(Collectors.toList());
    }

    public ChatRoomDto enterRoomUser(Long roomId, Long userProfileId) {
        ChatRooms chatRooms = verifyRoom(roomId);
        UserProfiles userProfiles = verifyUserProfile(userProfileId);

        ChatMembers chatMembers = ChatMembers.builder()
                .userProfiles(userProfiles)
                .chatRooms(chatRooms)
                .build();
        chatRooms.addUser(chatMembers);
        chatRooms.plusUserCount();

        return ChatRoomDto.builder()
                .roomId(chatRooms.getRoomId())
                .roomName(chatRooms.getName())
                .userProfileId(userProfiles.getId())
                .nickname(userProfiles.getNickname())
                .build();
    }

    // todo: 방장이 퇴장할 경우
    // todo: nickname 주석 authentication으로 처리
    public ChatRoomDto exitRoomUser(Long roomId, Long userProfileId) {
        ChatRooms chatRooms = verifyRoom(roomId);

        if(!chatRooms.removeUser(userProfileId)) throw new BusinessException(ApiResultStatus.USER_NOT_EXIST_ROOM);
        chatRooms.minusUserCount();

        chatRoomRepository.save(chatRooms);

        return ChatRoomDto.builder()
                .roomId(chatRooms.getRoomId())
                .roomName(chatRooms.getName())
                .userProfileId(userProfileId)
//                .nickname(userProfiles.getNickname())
                .build();
    }

    public List<UserProfileDto> getRoomUserList(Long roomId) {
        ChatRooms chatRooms = verifyRoom(roomId);

        return chatRooms.getChatMembers().stream()
                .map(chatRoomUserProfiles -> UserProfileDto.from(chatRoomUserProfiles.getUserProfiles()))
                .collect(Collectors.toList());
    }

    private ChatRooms verifyRoom(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(() -> new BusinessException(ApiResultStatus.ROOM_NOT_FOUND));
    }

    private UserProfiles verifyUserProfile(Long userProfileId) {
        return userProfileRepository.findById(userProfileId).orElseThrow(() -> new BusinessException(ApiResultStatus.USER_NOT_FOUND));
    }
}
