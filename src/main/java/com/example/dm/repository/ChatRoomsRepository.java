package com.example.dm.repository;

import com.example.dm.entity.ChatRooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomsRepository extends JpaRepository<ChatRooms, Long>, CustomChatRoomsRepository {
}
