package com.example.dm.chat.entity;

import com.example.dm.entity.UserProfiles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMembers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_profiles_id")
    private UserProfiles userProfiles;

    @ManyToOne
    @JoinColumn(name = "chat_rooms_id")
    private ChatRooms chatRooms;

    public static ChatMembers from(ChatRooms chatRooms, UserProfiles userProfiles) {
        return ChatMembers.builder()
                .userProfiles(userProfiles)
                .chatRooms(chatRooms)
                .build();
    }
}
