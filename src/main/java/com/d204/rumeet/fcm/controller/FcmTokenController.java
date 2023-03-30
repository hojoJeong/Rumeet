package com.d204.rumeet.fcm.controller;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.fcm.model.dto.FcmTokenDto;
import com.d204.rumeet.fcm.model.service.FcmMessageService;
import com.d204.rumeet.fcm.model.service.FcmTokenService;
import com.d204.rumeet.user.model.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Log4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/fcm")
public class FcmTokenController {
    @Autowired
    FcmTokenService fcmService;

    @Autowired
    FcmMessageService fcmMessageService;

    @PostMapping("/token")
    @Operation(summary = "새로운 토큰을 서버에 저장한다. (body에 id, token)",
            description = "성공시 flag = success, 실패시 flag=fail")
    public ResponseEntity<?> registToken(FcmTokenDto token){
        log.info("registToken : token:"+token);
        RespData<Void>data = new RespData<>();
        if(fcmService.updateUserToken(token) == 1) {
            data.setFlag("success");
            data.setMsg("FCM Token 등록 완료");
            data.setCode(0);
        } else {
            data.setFlag("fail");
            data.setMsg("일치하는 회원이 없습니다.");
            data.setCode(1);
        }
        return data.builder();
    }

    @PostMapping("/sendMessageTo")
    public void sendMessageTo(String token, String title, String body) throws IOException {
        log.info("sendMessageTo : token:"+token+", title:"+title+", body:"+body);
        fcmMessageService.sendMessageTo(token, title, body);
    }

}
