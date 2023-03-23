package com.d204.rumeet.chat.model.dao;

import com.d204.rumeet.chat.model.dto.ChatDto;
import com.d204.rumeet.chat.model.dto.LastChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatDao {
    private final MongoTemplate mongoTemplate;
    public void saveChat(ChatDto chatDto) {
        mongoTemplate.save(chatDto);
        mongoTemplate.remove(Query.query(Criteria.where("roomId").is(chatDto.getRoomId())), LastChatDto.class);
        mongoTemplate.save(chatDto,"LastChatDto");
    }

    public List<ChatDto> getChatByRoomId(int roomId) {
        return mongoTemplate.find(Query.query(Criteria.where("roomId").is(roomId)), ChatDto.class);
    }

    public List<LastChatDto> getLastChatList(int userId) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("toUserId").is(userId),
                Criteria.where("fromUserId").is(userId)
        );
        query.addCriteria(criteria);
        return mongoTemplate.find(query , LastChatDto.class);
    }
    public List<ChatDto> getLastChatList2(int userId) {
        return mongoTemplate.find(new Query(Criteria.where("fromUserId").is(userId).orOperator(Criteria.where("toUserId").is(userId)))
                , ChatDto.class);
    }
}
