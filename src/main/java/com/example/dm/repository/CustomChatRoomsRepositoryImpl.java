package com.example.dm.repository;

import com.example.dm.entity.ChatRooms;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.dm.entity.QChatMembers.chatMembers;
import static com.example.dm.entity.QChatRooms.chatRooms;
import static com.example.dm.entity.QUserProfiles.userProfiles;

@Repository
@RequiredArgsConstructor
public class CustomChatRoomsRepositoryImpl implements CustomChatRoomsRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChatRooms> findUserByRoomId(Long roomId) {
        return jpaQueryFactory.selectFrom(chatRooms)
                .join(chatRooms.chatMembers, chatMembers).fetchJoin()
                .join(chatMembers.userProfiles, userProfiles).fetchJoin()
                .where(chatRooms.id.eq(roomId))
                .fetch();
    }
}
