package com.d204.rumeet.chat.model.service;

import com.d204.rumeet.chat.model.dao.ChatDao;
import com.d204.rumeet.chat.model.dto.*;
import com.d204.rumeet.chat.model.mapper.ChatMapper;
import com.d204.rumeet.user.model.dto.UserDto;
import com.d204.rumeet.user.model.service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{

    private final ChatMapper chatMapper;
    private final ChatDao chatDao;
    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;
    private final UserService userService;

    @Override
    public ChatRoomDto makeRoom(MakeChatRoomDto makeChatRoomDto) {
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setUser1(makeChatRoomDto.getUser1());
        chatRoomDto.setUser2(makeChatRoomDto.getUser2());
        chatRoomDto.setDate(System.currentTimeMillis());
        chatMapper.makeRoom(chatRoomDto);
        return chatRoomDto;
    }

    @Override
    public ChatDto convertChat(Message message) {
        String str = new String(message.getBody());
        ChatDto chat = new Gson().fromJson(str,ChatDto.class);
        chat.setDate(System.currentTimeMillis());
        log.info("{}", chat);
        return chat;
    }

    @Override
    public void saveChat(ChatDto chat) {
        chatDao.saveChat(chat);
    }

    @Override
    public void doChat(ChatDto chat, Message message) {
        StringBuilder sb = new StringBuilder();
        sb.append("room.").append(chat.getRoomId()).append(".").append(chat.getToUserId());
        rabbitTemplate.send("chat.exchange",sb.toString(),message);
    }

    @Override
    public ChatRoomDto getChatRoom(int userId) {

        return chatMapper.getChatRoom(userId);
    }

    @Override
    public List<ChatDto> getChatByRoomId(int roomId) {
        return chatDao.getChatByRoomId(roomId);
    }

    @Override
    public void createQueue(ChatRoomDto chatRoomDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("room.").append(chatRoomDto.getId()).append(".").append(chatRoomDto.getUser1());
        Queue queue = QueueBuilder.durable("chat.queue." + chatRoomDto.getId()+"."+chatRoomDto.getUser1()).build();
        amqpAdmin.declareQueue(queue);
        Binding binding = BindingBuilder.bind(queue)
                .to(new TopicExchange("chat.exchange"))
                .with(sb.toString());
        amqpAdmin.declareBinding(binding);


        sb = new StringBuilder();
        sb.append("room.").append(chatRoomDto.getId()).append(".").append(chatRoomDto.getUser2());
        queue = QueueBuilder.durable("chat.queue." + chatRoomDto.getId()+"."+chatRoomDto.getUser2()).build();

        amqpAdmin.declareQueue(queue);
        binding = BindingBuilder.bind(queue)
                .to(new TopicExchange("chat.exchange"))
                .with(sb.toString());
        amqpAdmin.declareBinding(binding);
    }

    @Override
    public List<ChatRoomListDto> getChatRoomList(int userId) {
        List<ChatRoomListDto> list = new ArrayList<>();
        System.out.println(userId);
        List<LastChatDto> lastChatDtos = chatDao.getLastChatList(userId);
        int size = lastChatDtos.size();
//        log.info("{}", chatDao.getLastChatList2(userId).size());
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        QueueInformation queueInformation = null;
        for(int i = size - 1; i >= 0; i--) {
            LastChatDto lastChatDto = lastChatDtos.get(i);
            ChatRoomListDto dto = new ChatRoomListDto();
            int otherUserId = lastChatDto.getFromUserId();
            if(userId == otherUserId) {
                otherUserId = lastChatDto.getToUserId();
            }
            UserDto userDto = userService.getUserById(otherUserId);
            dto.setRoomId(lastChatDto.getRoomId());
            dto.setUserId(otherUserId);
            dto.setContent(lastChatDto.getContent());
            dto.setProfile(userDto.getProfile());
            dto.setNickname(userDto.getNickname());
            StringBuilder queueName = new StringBuilder();
            queueName.append("chat.queue.").append(lastChatDto.getRoomId()).append(".").append(userId);
            queueInformation = rabbitAdmin.getQueueInfo(queueName.toString());
            dto.setNoReadCnt(queueInformation.getMessageCount());
            list.add(dto);
        }

        return list;
    }
}
