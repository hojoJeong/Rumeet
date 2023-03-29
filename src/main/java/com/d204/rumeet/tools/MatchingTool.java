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
    public void doMatching(GamePaceDto target){
        int userIndex = 0;
        int topK = 5;

        double similarities = 0;
        double top_val = 0;
        GamePaceDto top_user = null;
        Node node = list.head;
        while (node != null) {
            similarities = calculateEuclideanSimilarity(node.user.getPace(), target.getPace());
            if(similarities >= 0.01) {
                if(top_val < similarities) {
                    top_user = node.user;
                    top_val = similarities;
                }
            }
        }
        if(top_user != null) {
            list.remove(top_user.getId());
//            kafkaService.sendMessage(topic, new Gson().toJson(target));
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


    class LinkedList {
        Node head;
        Node tail;

        void add(GamePaceDto target) {
            Node node = new Node(target);
            this.tail.next = node;
            this.tail = node;
        }

        void remove(int userId) {
            Node node = this.head;
            if(this.head.user.getId() == userId) {
                this.head = this.tail = null;
                return;
            }
            Node prev = node;
            node = node.next;
            while (node !=null) {
                if(node.user.getId() == userId) {
                    prev.next = node.next;
                    if(node == this.tail) {
                        this.tail = prev;
                    }
                    break;
                }
                prev = node;
                node = node.next;
            }
        }
    }
    class Node {
        GamePaceDto user;
        Node next;

        public Node(GamePaceDto user) {
            this.user = user;
        }
    }
}
