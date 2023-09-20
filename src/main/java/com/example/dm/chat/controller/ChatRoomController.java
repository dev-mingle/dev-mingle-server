package com.example.dm.chat.controller;

import com.example.dm.chat.dto.ChatCreateDto;
import com.example.dm.chat.dto.ChatMessageGetDto;
import com.example.dm.chat.dto.ChatRoomCreateDto;
import com.example.dm.chat.dto.ChatRoomDetailDto;
import com.example.dm.chat.service.ChatMessageService;
import com.example.dm.chat.service.ChatRoomService;
import com.example.dm.controller.BaseController;
import com.example.dm.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import static com.example.dm.chat.controller.ChatMessageController.SUBSCRIBE_URL;

@RestController
@RequestMapping("/v1/chats")
@RequiredArgsConstructor
public class ChatRoomController extends BaseController {

    private final ChatRoomService chatRoomService;

    private final ChatMessageService chatMessageService;

    private final SimpMessageSendingOperations template;

    @PostMapping
    public ResponseEntity<ApiResponse> createRoom(@RequestBody ChatRoomCreateDto chatRoomCreateDto) {
        return responseBuilder(chatRoomService.createRoom(chatRoomCreateDto), HttpStatus.CREATED);
    }

    /**
     *  todo: @AuthenticationPrincipal 사용고려
     */
    @PostMapping("/{roomId}/join/{userProfileId}")
    public ResponseEntity<ApiResponse> joinRoom(@PathVariable Long roomId,
                                                @PathVariable Long userProfileId) {
        ChatRoomDetailDto chatRoomDetailDto = chatRoomService.enterRoomUser(roomId, userProfileId);

        String message = chatRoomDetailDto.getNickname() + "님이 입장하였습니다.";
        processMessage(roomId, chatRoomDetailDto, message);

        return responseBuilder(chatRoomDetailDto, HttpStatus.OK);
    }

    @GetMapping("/user/{userProfileId}/rooms")
    public ResponseEntity<ApiResponse> getRoomsByUserProfileId(@PathVariable Long userProfileId) {
        return responseBuilder(chatRoomService.findRoomByUser(userProfileId), HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse> getRoomMessage(@PathVariable Long roomId) {
        return responseBuilder(chatMessageService.findAllMessageByRoomId(roomId), HttpStatus.OK);
    }

    @DeleteMapping("/{roomId}/exit/{userProfileId}")
    public ResponseEntity<ApiResponse> exitRoom(@PathVariable Long roomId,
                                                @PathVariable Long userProfileId) {
        ChatRoomDetailDto chatRoomDetailDto = chatRoomService.exitRoomUser(roomId, userProfileId);

        String message = chatRoomDetailDto.getNickname() + "님이 퇴장하였습니다.";
        processMessage(roomId, chatRoomDetailDto, message);

        return responseBuilder(chatRoomDetailDto, HttpStatus.OK);
    }
    @GetMapping("/{roomId}/userlist")
    public ResponseEntity<ApiResponse> getUserList(@PathVariable Long roomId) {
        return responseBuilder(chatRoomService.getRoomUserList(roomId), HttpStatus.OK);
    }

    private void processMessage(Long roomId, ChatRoomDetailDto chatRoomDetailDto, String message) {
        ChatCreateDto chatCreateDto = ChatCreateDto.from(chatRoomDetailDto, message);
        ChatMessageGetDto chatMessageGetDto = chatMessageService.saveMessage(chatCreateDto);
        template.convertAndSend(SUBSCRIBE_URL + roomId, chatMessageGetDto);
    }
}
