package com.example.dm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 팔로우 리스트 조회 시 정렬 방식 Enum
 */
@Getter
@AllArgsConstructor
public enum OrderType {
    latest, // 최신 순(desc)
    nickname // 닉네임 순(asc)
}
