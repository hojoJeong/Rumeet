package com.d204.rumeet.user.controller;

import com.d204.rumeet.data.HttpVO;
import com.d204.rumeet.user.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class RestUserController {
    HttpVO http = null;
    final UserService userService;

    @GetMapping
    public ResponseEntity<?> list() {
        System.out.println("회원조회 API");
        try {
            http = userService.list();
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(new ResponseEntity<HttpVO>(http, HttpStatus.OK));
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }
}
