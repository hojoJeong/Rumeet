package com.d204.rumeet.game.handler;

import com.d204.rumeet.game.model.dto.GameDto;
import com.d204.rumeet.game.model.dto.FriendRaceDto;
import com.d204.rumeet.game.model.dto.RaceDto;
import com.d204.rumeet.game.model.service.GameService;
import com.d204.rumeet.tools.FriendMatchingTool;
import com.d204.rumeet.tools.MatchingTool;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.spark.internal.config.R;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameHandler {

    private final GameService gameService;
    private final MatchingTool matchingTool;
    private final FriendMatchingTool friendMatchingTool;
    private final MongoTemplate mongoTemplate;

    @RabbitListener(queues = "matching.queue")
    public void matchingHandler(Message message) {
        String msg = new String(message.getBody());
        GameDto info = new Gson().fromJson(msg, GameDto.class);
        System.out.println(info);
        try {
            matchingTool.doMatching(info);
        } catch (Exception e) {
            System.out.println("매칭 오류");
        }
    }
    @RabbitListener(queues = "cancel.queue")
    public void cancelHandler(Message message) {
        String msg = new String(message.getBody());
        GameDto info = new Gson().fromJson(msg, GameDto.class);
        try {
            matchingTool.doCancel(info);
        } catch (Exception e) {
            System.out.println(msg);
            System.out.println("캔슬 오류");
        }
    }

    @RabbitListener(queues = "end.queue")
    public void endHandler(Message message) {
        try {
            gameService.endGameToKafka(message);
        } catch (Exception e) {
            System.out.println("기록저장 오류");
        }
    }


    // 초대 수락시 roomId가 여기 들어옴
    @RabbitListener(queues ="friend.queue")
    public void matchingWithFriendHandler(Message message) {
        // 큐에 있는 roomId 확인한 뒤에 mongoDB 에 담긴 유저 보고
        // friend.user.{userId} , friend.user.{partnerId} 여기로
        // 레이스 정보 보내주기
        String msg = new String(message.getBody());
        int raceId = Integer.parseInt(msg); // raceId만 있으므로
        System.out.println("#########################################friend.queue에 들어온 raceId : "+raceId);
        // mongoDB에서 정보 읽어오기
        FriendRaceDto friendRaceDto = mongoTemplate.findOne(Query.query(
                Criteria.where("raceId").is(raceId)),
                FriendRaceDto.class
        );
        //raceId
        RaceDto raceDto = gameService.getRace(raceId);
        System.out.println("############### 매칭된 게임 정보"+raceDto.toString());
        if (friendRaceDto.getState() != -1) { // 달리기 시작
            friendMatchingTool.doRunning(raceDto);
        }
    }

}
