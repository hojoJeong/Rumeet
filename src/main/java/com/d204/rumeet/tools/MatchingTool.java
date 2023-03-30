package com.d204.rumeet.tools;

import com.d204.rumeet.game.model.dto.GameDto;
import com.d204.rumeet.game.model.dto.GamePaceDto;
import com.d204.rumeet.game.model.dto.RaceDto;
import com.d204.rumeet.game.model.service.GameService;
import com.d204.rumeet.game.model.service.KafkaService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchingTool {


    private final LinkedList[] lists = new LinkedList[20];
    private final KafkaService kafkaService;

    private final GameService gameService;

    //이번
    // 매칭 시작하는 것
    public void doMatching(GameDto info) {
        int mode = info.getMode();
        int[] km = new int[] {1,2,3,5,1,2,3,5,1,2,3,5};
        if(lists[mode] == null) {
            lists[mode] = new LinkedList();
        }

        LinkedList list = lists[mode];

        GamePaceDto target = kafkaService.messageBYFastApi(km[mode], info.getUserId());

        double similarities = 0;
        double top_val = 0;
        GamePaceDto top_user = null;
        Node node = list.head;
        while (node != null) {
            similarities = calculateEuclideanSimilarity(node.user.getPace(), target.getPace());
            if (similarities >= 0.01) {
                if (top_val < similarities) {
                    top_user = node.user;
                    top_val = similarities;
                }
            }
            node = node.next;
        }

        if (top_user != null) {
            list.remove(top_user.getId());
            RaceDto raceDto = new RaceDto();
            raceDto.setMode(mode);
            raceDto.setDate(System.currentTimeMillis());
            raceDto.setUserId(top_user.getId());
            raceDto.setPartnerId(target.getId());
            gameService.makeRace(raceDto);
            String json = new Gson().toJson(raceDto);

            System.out.println("top_user = " + top_user);
            System.out.println("target = " + target);
        } else {
            list.add(target);
        }
        list.print();
        System.out.println("========================");
    }

    public void doCancel(GameDto gameDto) {
        lists[gameDto.getMode()].remove(gameDto.getUserId());
        lists[gameDto.getMode()].print();
    }

    private static double calculateEuclideanSimilarity(int[] user1, int[] user2) {
        double distance = 0;

        for (int i = 0; i < user1.length; i++) {
            distance += Math.pow(user1[i] - user2[i], 2);
        }

        return 1 / (1 + Math.sqrt(distance));
    }

}
