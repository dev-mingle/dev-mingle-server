package com.example.dm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"user_profiles_id", "chat_rooms_id"})})
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
