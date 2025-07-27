package com.mysite.weatherviewer.interceptor;

import com.mysite.weatherviewer.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@NoArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {
    private SessionService sessionService;

    public SessionInterceptor(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("Hello");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
