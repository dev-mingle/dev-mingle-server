package com.example.dm.chat.entity;

import com.example.dm.entity.BaseTimeEntity;
import com.example.dm.entity.UserProfiles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    private String name;

    private int userCount;

    @ManyToOne
    @JoinColumn(name = "admin_user")
    private UserProfiles adminUser;

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomUserProfiles> userProfiles = new ArrayList<>();

    public void plusUserCount() {
        this.userCount++;
    }

    public void minusUserCount() {
        this.userCount--;
    }

    public void addUser(ChatRoomUserProfiles chatRoomUserProfiles){
        userProfiles.add(chatRoomUserProfiles);
    }

    public boolean removeUser(Long userProfileId) {
        return this.getUserProfiles()
                .removeIf(chatRoomUserProfiles -> chatRoomUserProfiles.getUserProfiles().getId().equals(userProfileId));
    }

}
