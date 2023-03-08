package com.d204.rumeet.demo.hello.controller;

import com.d204.rumeet.demo.data.Sample;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloRestController {

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        Sample<?> sample = new Sample<>("flag", "msg", "data");

        ResponseEntity<?> sampleJson = new ResponseEntity<Sample>(sample, HttpStatus.OK);
        // Array List Return
        System.out.println(sampleJson);
        return sampleJson;
    }
    
    @GetMapping("/hello2")
    public ResponseEntity<?> hello2() {
        Sample<int[]> sample2 = new Sample<>();
        sample2.setFlag("success");
        sample2.setMsg("new msg");
        List<int[]> list = new ArrayList<>();
        list.add(new int[]{1,2,3});
        sample2.setData(new int[]{1, 2, 3});
        System.out.println(sample2);
        return sample2.builder();
    }
}

