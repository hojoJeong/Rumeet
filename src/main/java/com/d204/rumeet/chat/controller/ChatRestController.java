package com.d204.rumeet.chat.controller;

import com.d204.rumeet.chat.model.dto.ChatRoomDataDto;
import com.d204.rumeet.chat.model.dto.ChatRoomDto;
import com.d204.rumeet.chat.model.dto.CreateChatReturnDTO;
import com.d204.rumeet.chat.model.dto.CreateChatRoomDto;
import com.d204.rumeet.chat.model.service.ChatService;
import com.d204.rumeet.data.RespData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/chat")
@RequiredArgsConstructor
@RestController
public class ChatRestController {

    private final ChatService chatService;

    @Operation(summary = "방 정보와 전체 채팅 메시지 기록 조회")
    @GetMapping("/{id}")
    public ResponseEntity<?> getChatByRoomId(@PathVariable int id) {
        RespData<ChatRoomDataDto> data = new RespData<>();
        data.setData(chatService.getChatByRoomId(id));
        return data.builder();
    }

    @Operation(summary = "채팅 목록 조회")
    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getChatList(@PathVariable int userId) {
        RespData<List> data = new RespData<>();
        data.setData(chatService.getChatRoomList(userId));
        return data.builder();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody CreateChatRoomDto chatRoomDto) {
        RespData<CreateChatReturnDTO> data = new RespData<>();
        data.setData(chatService.createRoom(chatRoomDto));
        return data.builder();
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteRoomById(@RequestParam("id") int roomId,@RequestParam("userId") int userId) {
        RespData<Void> data = new RespData<>();
        chatService.deleteRoomById(roomId,userId);
        data.setData(null);
        return data.builder();
    }


}
