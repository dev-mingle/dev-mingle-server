package com.example.dm.repository;

import com.example.dm.entity.ChatRooms;

import java.util.List;

public interface CustomChatRoomsRepository {
    public List<ChatRooms> findUserByRoomId(Long roomId);
}
