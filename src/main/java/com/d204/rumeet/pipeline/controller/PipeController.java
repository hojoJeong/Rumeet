package com.d204.rumeet.pipeline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PipeController {

    @GetMapping("/test")
    public String index() {
        return "index";
    }

}
