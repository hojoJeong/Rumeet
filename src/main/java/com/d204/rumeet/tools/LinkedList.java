package com.d204.rumeet.tools;

import com.d204.rumeet.game.model.dto.GamePaceDto;
import org.springframework.stereotype.Component;

@Component
public class LinkedList {
    Node head;
    Node tail;

    void add(GamePaceDto target) {
        if(this.head == null) {
            this.head = this.tail = new Node(target);
            return;
        }
        Node node = new Node(target);
        this.tail.next = node;
        this.tail = node;
    }

    void remove(int userId) {
        Node node = this.head;
        if (this.head.user.getId() == userId) {
            if(this.head == this.tail) {
                this.head = this.tail = null;
            } else {
                Node next = this.head.next;
                this.head.next = null;
                this.head = next;
            }
            return;
        }
        Node prev = node;
        node = node.next;
        while (node != null) {
            if (node.user.getId() == userId) {
                prev.next = node.next;
                if (node == this.tail) {
                    this.tail = prev;
                }
                break;
            }
            prev = node;
            node = node.next;
        }
    }
    void print() {
        Node node = this.head;
        System.out.println("큐 출력@@@@@@@@@@@@@@@@2");
        while (node != null) {
            System.out.print(node.user + " ");
            node = node.next;
        }
        System.out.println();
    }
}


