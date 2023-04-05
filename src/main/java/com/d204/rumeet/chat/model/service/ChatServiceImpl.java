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
        if(chat.getRoomId() == -1 || chat.getToUserId() ==-1 || chat.getFromUserId() ==-1) return;
        rabbitTemplate.send("chat.exchange",sb.toString(),message);
        List<ChatRoomListDto> fromUserList = this.getChatRoomList(chat.getFromUserId());
        List<ChatRoomListDto> toUserList = this.getChatRoomList(chat.getToUserId());
        Gson gson = new Gson();

        rabbitTemplate.convertAndSend("user.exchange","user."+chat.getFromUserId(),gson.toJson(fromUserList));
        rabbitTemplate.convertAndSend("user.exchange","user."+chat.getToUserId(),gson.toJson(toUserList));
    }

    @Override
    public ChatRoomDto getChatRoom(int userId) {

        return chatMapper.getChatRoom(userId);
    }

    @Override
    public ChatRoomDataDto getChatByRoomId(int roomId) {
        ChatRoomDataDto chatRoomDataDto = new ChatRoomDataDto();
        ChatRoomDto chatRoomDto = chatMapper.getRoomById(roomId);
        chatRoomDataDto.setId(roomId);
        chatRoomDataDto.setUser1(chatRoomDto.getUser1());
        chatRoomDataDto.setUser2(chatRoomDto.getUser2());
        chatRoomDataDto.setDate(chatRoomDto.getDate());
        chatRoomDataDto.setState(chatRoomDto.getState());
        chatRoomDataDto.setChat(chatDao.getChatByRoomId(roomId));

        return chatRoomDataDto;
    }

    @Override
    public void deleteRoomById(int id, int userId) {
        chatDao.deleteLastChat(id,userId);
        chatMapper.deleteRoomById(id);
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
    public CreateChatReturnDTO createRoom(CreateChatRoomDto dto) {
        CreateChatReturnDTO returnDTO;

        ChatRoomDto chatRoomDto = chatMapper.findChatRoom(dto);
        if(chatRoomDto == null) {
            chatRoomDto = new ChatRoomDto();
            chatRoomDto.setUser1(dto.getUser1());
            chatRoomDto.setUser2(dto.getUser2());
            chatRoomDto.setDate(System.currentTimeMillis());
            chatMapper.makeRoom(chatRoomDto);
            createQueue(chatRoomDto);
        }
        UserDto user2 = userService.getUserById(dto.getUser2());
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        QueueInformation queueInformation = null;
        StringBuilder queueName = new StringBuilder();
        queueName.append("chat.queue.").append(chatRoomDto.getId()).append(".").append(dto.getUser1());
        queueInformation = rabbitAdmin.getQueueInfo(queueName.toString());
        returnDTO = new CreateChatReturnDTO(chatRoomDto.getId(), chatRoomDto.getUser2()
                ,user2.getNickname(),user2.getProfile(),queueInformation.getMessageCount());
        return returnDTO;
    }

    @Override
    public List<ChatRoomListDto> getChatRoomList(int userId) {
        List<ChatRoomListDto> list = new ArrayList<>();
        System.out.println(userId);
        List<LastChatDto> lastChatDtos = chatDao.getLastChatList(userId);
        int size = lastChatDtos.size();
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
            dto.setDate(lastChatDto.getDate());
            StringBuilder queueName = new StringBuilder();
            queueName.append("chat.queue.").append(lastChatDto.getRoomId()).append(".").append(userId);
            queueInformation = rabbitAdmin.getQueueInfo(queueName.toString());
            dto.setNoReadCnt(queueInformation.getMessageCount());
            list.add(dto);
        }

        return list;
    }
}
