package com.d204.rumeet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@MapperScan(basePackages = "com.d204.rumeet.*.model.mapper")
@EnableAspectJAutoProxy(proxyTargetClass=true)
@SpringBootApplication
public class RumeetApplication {

    public static void main(String[] args) {
        SpringApplication.run(RumeetApplication.class, args);
    }

}
