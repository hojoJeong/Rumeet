package com.d204.rumeet.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .version("v1.0.0")
                .title("Rumeet API")
                .description("API");

        String jwtSchemeName = "jwtAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    @Bean
    public GroupedOpenApi group() {
        return GroupedOpenApi.builder()
                .group("전체")
                .packagesToScan("com.d204.rumeet") // package 필터 설정
                .build();
    }
    @Bean
    public GroupedOpenApi group1() {
        return GroupedOpenApi.builder()
                .group("user")
                .packagesToScan("com.d204.rumeet.user") // package 필터 설정
                .build();
    }

    @Bean
    public GroupedOpenApi group2() {
        return GroupedOpenApi.builder()
                .group("friend")
                .packagesToScan("com.d204.rumeet.friend") // package 필터 설정
                .build();
    }

    @Bean
    public GroupedOpenApi group3() {
        return GroupedOpenApi.builder()
                .group("friend")
                .packagesToScan("com.d204.rumeet.chat") // package 필터 설정
                .build();
    }

    @Bean
    public GroupedOpenApi group4() {
        return GroupedOpenApi.builder()
                .group("friend")
                .packagesToScan("com.d204.rumeet.game") // package 필터 설정
                .build();
    }

}