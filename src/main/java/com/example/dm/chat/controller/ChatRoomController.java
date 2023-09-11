package com.example.dm.chat.controller;

import com.example.dm.chat.dto.ChatRoomDto;
import com.example.dm.chat.service.ChatMessageService;
import com.example.dm.chat.service.ChatRoomService;
import com.example.dm.controller.BaseController;
import com.example.dm.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<ApiResponse> createRoom(@RequestBody ChatRoomDto chatRoomDto) {

        return responseBuilder(chatRoomService.createRoom(chatRoomDto), HttpStatus.CREATED);
    }
}
