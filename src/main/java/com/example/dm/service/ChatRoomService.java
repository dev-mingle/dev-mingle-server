package com.example.dm.service;

import com.example.dm.dto.chats.ChatRoomCreateDto;
import com.example.dm.dto.chats.ChatRoomDetailDto;
import com.example.dm.dto.chats.ChatRoomGetDto;
import com.example.dm.dto.chats.UserProfileGetDto;
import com.example.dm.entity.ChatMembers;
import com.example.dm.entity.ChatRooms;
import com.example.dm.entity.LoginUser;
import com.example.dm.entity.UserProfiles;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.BusinessException;
import com.example.dm.repository.ChatRoomsRepository;
import com.example.dm.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChatRoomsRepository chatRoomsRepository;
    private final UserProfileRepository userProfileRepository;

    public ChatRoomGetDto createRoom(ChatRoomCreateDto chatRoomCreateDto, LoginUser user) {
        UserProfiles userProfiles = verifyUserProfile(user.getId());
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
        }).toList();
    }

    public ChatRoomDetailDto enterRoomUser(Long roomId, LoginUser user) {
        ChatRooms chatRooms = verifyRoom(roomId);
        if(chatRooms.getCapacity() == chatRooms.getUserCount()) throw new BusinessException(ApiResultStatus.ROOM_FULL);
        UserProfiles userProfiles = verifyUserProfile(user.getId());
        ChatMembers chatMembers = ChatMembers.from(chatRooms, userProfiles);

        chatRooms.addUser(chatMembers);
        chatRooms.plusUserCount();

        return ChatRoomDetailDto.from(chatRooms, chatRooms.getAdminUser(), user);
    }

    // todo: 방장이 퇴장할 경우
    public ChatRoomDetailDto exitRoomUser(Long roomId, LoginUser user) {
        ChatRooms chatRooms = verifyRoom(roomId);

        if(!chatRooms.removeUser(user.getId())) throw new BusinessException(ApiResultStatus.USER_NOT_EXIST_ROOM);
        chatRooms.minusUserCount();

        return ChatRoomDetailDto.from(chatRooms, chatRooms.getAdminUser(), user);
    }

    public List<UserProfileGetDto> getRoomUserList(Long roomId) {
        ChatRooms chatRooms = verifyRoom(roomId);

        return chatRooms.getChatMembers().stream()
                .map(chatRoomUserProfiles -> UserProfileGetDto.from(chatRoomUserProfiles.getUserProfiles()))
                .toList();
    }

    private ChatRooms verifyRoom(Long roomId) {
        return chatRoomsRepository.findById(roomId).orElseThrow(() -> new BusinessException(ApiResultStatus.ROOM_NOT_FOUND));
    }

    private UserProfiles verifyUserProfile(Long userProfileId) {
        return userProfileRepository.findById(userProfileId).orElseThrow(() -> new BusinessException(ApiResultStatus.USER_NOT_FOUND));
    }
}