package com.example.picture.interceptor;


import cn.dev33.satoken.stp.StpUtil;
import com.example.picture.exception.ExceptionEnum;
import com.example.picture.util.ResultResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class Interceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().contains("login")) {
            return true;
        }
        try {
            if (StpUtil.isLogin()) {
                return true;
            }
        } catch (Exception ignored) {
        }
        response.setStatus(403);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println(ResultResponse.error(ExceptionEnum.UNAUTHORIZED));
        return false;
    }
}
