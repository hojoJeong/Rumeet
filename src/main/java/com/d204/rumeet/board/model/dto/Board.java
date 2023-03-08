package com.d204.rumeet.board.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    private int id;
    private String title;
    private String content;
}


// depth 순으로 한다(위에서 아래로)
//controller > 구상시작 > service > serviceImpl > mapper > controller