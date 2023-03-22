package com.d204.rumeet.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

//@Configuration
//@EnableRabbit
public class RabbitConfig {
//
//    private static final String CHAT_QUEUE_NAME = "chat.queue";
//    private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
//    private static final String ROUTING_KEY = "room.*";
//
//    // Queue 등록
//    @Bean
//    public Queue queue() {
//        return new Queue(CHAT_QUEUE_NAME, true);
//    }
//
//    // Exchange 등록
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange(CHAT_EXCHANGE_NAME);
//    }
//
//    // Exchange 와 Queue 바인딩
//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(queue()).to(exchange()).with(ROUTING_KEY);
//    }
//
//
//    @Bean
//    public  com.fasterxml.jackson.databind.Module dateTimeModule() {
//        return new JavaTimeModule();
//    }
//
//
//    // Spring 에서 자동생성해주는 ConnectionFactory 는 SimpleConnectionFactory
//    // 여기서 사용하는 건 CachingConnectionFactory 라 새로 등록해줌 (RabbitMQ와 연결된 커넥션을 캐시하여 재사용할 수 있음)
//    // RabbitMQ와 연결하는데 필요한 정보
//    // RabbitMQ와 통신할 때는 이 ConnectionFactory를 사용하여 RabbitTemplate 등을 생성하고, 메시지를 보내거나 받아
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory factory = new CachingConnectionFactory();
//        factory.setHost("j8d204.p.ssafy.io");
//        factory.setUsername("guest");
//        factory.setPassword("guest");
//        return factory;
//    }
//
//    /**
//     * messageConverter를 커스터마이징 하기 위해 Bean 새로 등록
//     */
//
//
//    //RabbitTemplate : 메시지를 RabbitMQ 브로커에 보내거나 브로커에서 메시지를 가져올 때 사용
//    @Bean
//    public RabbitTemplate rabbitTemplate() {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
//        rabbitTemplate.setMessageConverter(jsonMessageConverter()); //메시지가 자동으로 JSON 형식으로 직렬화되며, 브로커가 수신한 메시지 역시 JSON 형식으로 직렬화되어 처리
//        rabbitTemplate.setRoutingKey(CHAT_QUEUE_NAME); //RabbitTemplate이 메시지를 보낼 때 사용할 라우팅 키를 설정
//        return rabbitTemplate;
//    }
//
//    //메시지를 수신할 때 사용되는 SimpleMessageListenerContainer를 설정
//    //리스너 등록
//    @Bean
//    public SimpleMessageListenerContainer container() {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory());
//        container.setQueueNames(CHAT_QUEUE_NAME); //리스너가 구독(subscribe)할 대상 Queue의 이름을 설정
//        container.setMessageListener(messageListener()); // 메시지 리스너 설정 (messageListener() 메소드를 호출하여 구현한 리스너 객체를 지정)
//        return container;
//    }
//
//    @Bean
//    public MessageListener messageListener() { //RabbitMQ에서 메시지를 받으면 실행되는 콜백 함수로, 수신한 메시지를 처리하는 로직을 작성
//        return (message) -> {
//            System.out.println("Received message: " + new String(message.getBody()));
//        };
//    }
//
//
//    // RabbitMQ 메시지를 JSON 형태로 변환
//    @Bean
//    public Jackson2JsonMessageConverter jsonMessageConverter() {
//        //LocalDateTime serializable 을 위해
//        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
//        objectMapper.registerModule(dateTimeModule());
//        return new Jackson2JsonMessageConverter(objectMapper);
//    }

}