package com.d204.rumeet.tools;

import com.d204.rumeet.game.model.dto.GamePaceDto;
import org.springframework.stereotype.Component;

public class Node {
    GamePaceDto user;
    Node next;

    public Node(GamePaceDto user) {
        this.user = user;
    }
}
