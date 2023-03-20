package com.d204.rumeet.interceptor;

import com.d204.rumeet.tools.JwtTool;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {

    private final JwtTool jwtTool;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if( token == null || token.split(" ").length != 2 || !jwtTool.validateToken(token.split(" ")[1])) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"JWT 만료");
            return false;
        }
        return true;
    }
}
