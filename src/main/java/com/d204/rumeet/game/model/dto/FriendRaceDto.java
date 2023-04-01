package com.d204.rumeet.game.model.dto;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "matchRequest")
public class FriendRaceDto {
    int raceId;
    int userId;
    int partnerId;
    int mode;
    Long date;
    int state;
}
