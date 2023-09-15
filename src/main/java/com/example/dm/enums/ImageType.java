package com.example.dm.enums;

public enum ImageType {
    UserProfiles("사용자 프로필"), Posts("게시글"), Chats("채팅");

    private final String Name;

    ImageType(String name){
        this.Name = name;
    }

    public String getName() {
        return Name;
    }
}
