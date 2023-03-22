package com.d204.rumeet.config;

import lombok.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //"/stomp/chat" 엔드포인트를 등록하고 SockJS를 사용하도록 설정
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*") //안해도 무관
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setPathMatcher(new AntPathMatcher("."));  //브로커에서 사용할 URL 패턴 매칭을 위한 설정
        // 클라이언트가 /pub/chat.enter.1이라는 URL로 메시지를 보내면, 이를 chat.enter.1로 사용할 수 있음

        registry.setApplicationDestinationPrefixes("/pub"); //클라이언트가 메시지를 보내기 위한 prefix를 설정

        registry.enableStompBrokerRelay("/queue", "/topic", "/exchange", "/amq/queue")
                .setRelayHost("13.125.45.74")
                .setRelayPort(61613)
                .setSystemLogin("guest")
                .setSystemPasscode("guest")
                .setClientLogin("guest")
                .setClientPasscode("guest");;
        //메시지를 중계할 브로커를 설정
        //"/queue", "/topic", "/exchange", "/amq/queue" 등의 목적지로 메시지를 중계할 수 있음

    }
}