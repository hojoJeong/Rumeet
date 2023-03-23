package com.d204.rumeet.config;

import com.d204.rumeet.interceptor.UserInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
@PropertySource("classpath:/url.properties")
public class WebConfig implements WebMvcConfigurer {

    private final UserInterceptor userInterceptor;

    @Value("${exclude.path.patterns}")
    private String[] excludePathPatterns;

    @Value("${add.path.patterns}")
    private String[] addPathPatterns;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .order(1)
                .excludePathPatterns(excludePathPatterns)
                .addPathPatterns(addPathPatterns);
    }

}
