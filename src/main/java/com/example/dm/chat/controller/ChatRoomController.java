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
@RequestMapping("/v1/chats")
@RequiredArgsConstructor
public class ChatRoomController extends BaseController {

    private final ChatRoomService chatRoomService;

    private final ChatMessageService chatMessageService;

    @PostMapping
    public ResponseEntity<ApiResponse> createRoom(@RequestBody ChatRoomDto chatRoomDto) {

    @GetMapping("/user/{userProfileId}/rooms")
    public ResponseEntity<ApiResponse> getRoomsByUserProfileId(@PathVariable Long userProfileId) {
        return responseBuilder(chatRoomService.findRoomByUser(userProfileId), HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse> getRoomMessage(@PathVariable Long roomId) {
        return responseBuilder(chatMessageService.findAllMessageByRoomId(roomId), HttpStatus.OK);
    }

}
