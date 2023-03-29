package com.d204.rumeet.tools;

import com.d204.rumeet.game.model.dto.GamePaceDto;
import com.d204.rumeet.kafka.model.KafkaService;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchingTool {


    private final LinkedList list;
    private final KafkaService kafkaService;

    //이번
    // 매칭 시작하는 것
    public void doMatching(GamePaceDto target) {
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
        }
        if (top_user != null) {
            list.remove(top_user.getId());
            //TODO Race 생성

        } else {
            list.add(target);
        }
    }


    private static double calculateEuclideanSimilarity(int[] user1, int[] user2) {
        double distance = 0;

        for (int i = 0; i < user1.length; i++) {
            distance += Math.pow(user1[i] - user2[i], 2);
        }

        return 1 / (1 + Math.sqrt(distance));
    }

}
