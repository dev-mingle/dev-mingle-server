package com.example.dm.chat.entity;

import com.example.dm.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    // todo: 방장 User

    // todo: User 목록 M:N

    public void plusUserCount() {
        this.userCount++;
    }

    public void minusUserCount() {
        this.userCount--;
    }

}
