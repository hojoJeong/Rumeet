package com.d204.rumeet.chat.controller;

import com.d204.rumeet.chat.model.service.ChatService;
import com.d204.rumeet.data.RespData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/chat")
@RequiredArgsConstructor
@RestController
public class ChatRestController {

    private final ChatService chatService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getChatByRoomId(@PathVariable int id) {
        RespData<List> data = new RespData<>();
        data.setData(chatService.getChatByRoomId(id));
        return data.builder();
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<?> getChatList(@PathVariable int id) {
        RespData<List> data = new RespData<>();
        data.setData(chatService.getChatRoomList(id));
        return data.builder();
    }
}
