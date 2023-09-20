package com.example.dm.chat.controller;

import com.example.dm.chat.dto.ChatDto;
import com.example.dm.chat.dto.ChatRoomCreateDto;
import com.example.dm.chat.dto.ChatRoomDto;
import com.example.dm.chat.service.ChatMessageService;
import com.example.dm.chat.service.ChatRoomService;
import com.example.dm.controller.BaseController;
import com.example.dm.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import static com.example.dm.chat.controller.ChatController.SUBSCRIBE_URL;

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
     * todo: JOIN 메시지 전송 / mongodb
     *  todo: @AuthenticationPrincipal 사용고려
     */
    @PostMapping("/{roomId}/join/{userProfileId}")
    public ResponseEntity<ApiResponse> joinRoom(@PathVariable Long roomId,
                                                @PathVariable Long userProfileId) {
        ChatRoomDto chatRoomDto = chatRoomService.enterRoomUser(roomId, userProfileId);

        ChatDto chatDto = ChatDto.builder()
                .message(chatRoomDto.getNickname() + "님이 참여하였습니다.")
                .roomId(roomId)
                .sender(chatRoomDto.getNickname())
                .build();
        template.convertAndSend(SUBSCRIBE_URL + roomId, chatDto);

        return responseBuilder(chatRoomDto, HttpStatus.OK);
    }

    @GetMapping("/user/{userProfileId}/rooms")
    public ResponseEntity<ApiResponse> getRoomsByUserProfileId(@PathVariable Long userProfileId) {
        return responseBuilder(chatRoomService.findRoomByUser(userProfileId), HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse> getRoomMessage(@PathVariable Long roomId) {
        return responseBuilder(chatMessageService.findAllMessageByRoomId(roomId), HttpStatus.OK);
    }

    // todo: mongodb
    @DeleteMapping("/{roomId}/exit/{userProfileId}")
    public ResponseEntity<ApiResponse> exitRoom(@PathVariable Long roomId,
                                                @PathVariable Long userProfileId) {
        ChatRoomDto chatRoomDto = chatRoomService.exitRoomUser(roomId, userProfileId);

        ChatDto chatDto = ChatDto.builder()
                .message(chatRoomDto.getNickname() + "님이 퇴장하였습니다.")
                .roomId(roomId)
                .sender(chatRoomDto.getNickname())
                .build();
        template.convertAndSend(SUBSCRIBE_URL + roomId, chatDto);

        return responseBuilder(chatRoomDto, HttpStatus.OK);
    }

    @GetMapping("/{roomId}/userlist")
    public ResponseEntity<ApiResponse> getUserList(@PathVariable Long roomId) {
        return responseBuilder(chatRoomService.getRoomUserList(roomId), HttpStatus.OK);
    }
}
