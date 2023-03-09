package com.d204.rumeet.data;


import lombok.Data;

import java.util.List;

@Data
public class HttpVO {
    private String flag = "fail";
    private List<?> data;

}