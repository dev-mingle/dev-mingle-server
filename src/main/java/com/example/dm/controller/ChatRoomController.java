package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.dto.chats.*;
import com.example.dm.entity.LoginUser;
import com.example.dm.service.ChatMessageService;
import com.example.dm.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path.default}/chats")
@RequiredArgsConstructor
public class ChatRoomController extends BaseController {

    private final ChatRoomService chatRoomService;

    private final ChatMessageService chatMessageService;

    private final SimpMessageSendingOperations template;

    @Value("${api.path.default}")
    private String DEFAULT_URL;

    @PostMapping
    public ResponseEntity<ApiResponse> createRoom(@RequestBody @Valid ChatRoomCreateDto chatRoomCreateDto,
                                                  @AuthenticationPrincipal LoginUser user) {
        return responseBuilder(chatRoomService.createRoom(chatRoomCreateDto, user), HttpStatus.CREATED);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<ApiResponse> joinRoom(@PathVariable Long roomId,
                                                @AuthenticationPrincipal LoginUser user) {
        ChatRoomDetailDto chatRoomDetailDto = chatRoomService.enterRoomUser(roomId, user);

        String message = chatRoomDetailDto.getNickname() + "님이 입장하였습니다.";
        processMessage(roomId, message, user);

        return responseBuilder(chatRoomDetailDto, HttpStatus.OK);
    }

    @GetMapping("/user/rooms")
    public ResponseEntity<ApiResponse> getRoomsByUserProfileId(@AuthenticationPrincipal LoginUser user) {
        return responseBuilder(chatRoomService.findRoomByUser(user.getId()), HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse> getRoomMessage(@PathVariable Long roomId,
                                                      @AuthenticationPrincipal LoginUser user) {
        return responseBuilder(chatMessageService.findAllMessageByRoomId(roomId, user), HttpStatus.OK);
    }

    @DeleteMapping("/{roomId}/exit")
    public ResponseEntity<ApiResponse> exitRoom(@PathVariable Long roomId,
                                                @AuthenticationPrincipal LoginUser user) {
        ChatRoomDetailDto chatRoomDetailDto = chatRoomService.exitRoomUser(roomId, user);

        String message = chatRoomDetailDto.getNickname() + "님이 퇴장하였습니다.";
        processMessage(roomId, message, user);

        return responseBuilder(chatRoomDetailDto, HttpStatus.OK);
    }

    @GetMapping("/{roomId}/userlist")
    public ResponseEntity<ApiResponse> getUserList(@PathVariable Long roomId) {
        return responseBuilder(chatRoomService.getRoomUserList(roomId), HttpStatus.OK);
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<ApiResponse> patchRoom(@PathVariable Long roomId,
                                                 @RequestBody ChatRoomPatchDto chatRoomPatchDto,
                                                 @AuthenticationPrincipal LoginUser user) {
        return responseBuilder(chatRoomService.updateRoom(roomId, chatRoomPatchDto, user), HttpStatus.CREATED);
    }

    private void processMessage(Long roomId, String message, LoginUser user) {
        ChatCreateDto chatCreateDto = ChatCreateDto.from(message);
        ChatMessageGetDto chatMessageGetDto = chatMessageService.saveMessage(chatCreateDto, roomId, user);
        template.convertAndSend("/sub" + DEFAULT_URL + "/chats/" + roomId, chatMessageGetDto);
    }
}
