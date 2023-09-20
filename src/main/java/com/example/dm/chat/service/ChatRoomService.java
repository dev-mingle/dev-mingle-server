package com.example.dm.chat.service;

import com.example.dm.chat.dto.ChatRoomCreateDto;
import com.example.dm.chat.dto.ChatRoomGetDto;
import com.example.dm.chat.dto.ChatRoomDetailDto;
import com.example.dm.chat.dto.UserProfileGetDto;
import com.example.dm.chat.entity.ChatMembers;
import com.example.dm.chat.entity.ChatRooms;
import com.example.dm.chat.repository.ChatRoomsRepository;
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

    private final ChatRoomsRepository chatRoomsRepository;
    private final UserProfileRepository userProfileRepository;

    public ChatRoomGetDto createRoom(ChatRoomCreateDto chatRoomCreateDto) {
        UserProfiles userProfiles = verifyUserProfile(chatRoomCreateDto.getAdminUserId());
        ChatRooms chatRooms = ChatRooms.from(chatRoomCreateDto, userProfiles);
        ChatMembers chatMembers = ChatMembers.from(chatRooms, userProfiles);

        chatRooms.addUser(chatMembers);
        ChatRooms chatRoom = chatRoomsRepository.save(chatRooms);

        return ChatRoomGetDto.from(chatRoom);
    }

    public ChatRooms findRoomById(Long roomId) {
        return verifyRoom(roomId);
    }

    // todo: 방장 표시
    public List<ChatRoomGetDto> findRoomByUser(Long userProfileId) {
        UserProfiles userProfiles = verifyUserProfile(userProfileId);

        return userProfiles.getChatMembers().stream().map(chatRoomUser -> {
            ChatRooms chatRooms = chatRoomUser.getChatRooms();
            return ChatRoomGetDto.from(chatRooms);
        }).collect(Collectors.toList());
    }

    public ChatRoomDetailDto enterRoomUser(Long roomId, Long userProfileId) {
        ChatRooms chatRooms = verifyRoom(roomId);
        UserProfiles userProfiles = verifyUserProfile(userProfileId);
        ChatMembers chatMembers = ChatMembers.from(chatRooms, userProfiles);

        chatRooms.addUser(chatMembers);
        chatRooms.plusUserCount();

        return ChatRoomDetailDto.from(chatRooms, chatRooms.getAdminUser(), userProfiles);
    }

    // todo: 방장이 퇴장할 경우
    public ChatRoomDetailDto exitRoomUser(Long roomId, Long userProfileId) {
        ChatRooms chatRooms = verifyRoom(roomId);

        if(!chatRooms.removeUser(userProfileId)) throw new BusinessException(ApiResultStatus.USER_NOT_EXIST_ROOM);
        chatRooms.minusUserCount();

        chatRoomsRepository.save(chatRooms);

        return ChatRoomDetailDto.from(chatRooms);
    }

    public List<UserProfileGetDto> getRoomUserList(Long roomId) {
        ChatRooms chatRooms = verifyRoom(roomId);

        return chatRooms.getChatMembers().stream()
                .map(chatRoomUserProfiles -> UserProfileGetDto.from(chatRoomUserProfiles.getUserProfiles()))
                .collect(Collectors.toList());
    }

    private ChatRooms verifyRoom(Long roomId) {
        return chatRoomsRepository.findById(roomId).orElseThrow(() -> new BusinessException(ApiResultStatus.ROOM_NOT_FOUND));
    }

    private UserProfiles verifyUserProfile(Long userProfileId) {
        return userProfileRepository.findById(userProfileId).orElseThrow(() -> new BusinessException(ApiResultStatus.USER_NOT_FOUND));
    }
}
