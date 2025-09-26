package com.mysite.weatherviewer.interceptor;

import com.mysite.weatherviewer.common.RequestAttributeKeys;
import com.mysite.weatherviewer.dto.SessionDto;
import com.mysite.weatherviewer.service.CookieService;
import com.mysite.weatherviewer.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

@Service
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final CookieService cookieService;
    private final SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Optional<Cookie> foundCookie = cookieService.findSessionCookie(request);
        if (foundCookie.isEmpty()) {
            response.sendRedirect("/auth/login");
            return false;
        }

        String sessionId = foundCookie.get().getValue();
        SessionDto foundSession = sessionService.findByUuid(sessionId);

        if (foundSession == null || !sessionService.isSessionActive(foundSession)) {
            if (foundSession != null) {
                sessionService.remove(sessionId);
            }

            cookieService.removeSessionCookie(foundCookie.get(), response);

            response.sendRedirect("/auth/login");
            return false;
        }

        request.setAttribute(RequestAttributeKeys.USER_COOKIE, foundCookie.get());
        request.setAttribute(RequestAttributeKeys.USER_SESSION, foundSession);

        return true;
    }
}
