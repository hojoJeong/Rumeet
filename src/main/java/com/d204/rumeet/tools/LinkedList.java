package com.d204.rumeet.tools;

import com.d204.rumeet.game.model.dto.GamePaceDto;
import org.springframework.stereotype.Component;

@Component
public class LinkedList {
    MatchingTool.Node head;
    MatchingTool.Node tail;

    void add(GamePaceDto target) {
        MatchingTool.Node node = new MatchingTool.Node(target);
        this.tail.next = node;
        this.tail = node;
    }

    void remove(int userId) {
        MatchingTool.Node node = this.head;
        if(this.head.user.getId() == userId) {
            this.head = this.tail = null;
            return;
        }
        MatchingTool.Node prev = node;
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
