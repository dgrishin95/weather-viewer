package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.dto.SessionDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${app.session.cookie.name}")
    private String cookieName;

    @Value("${app.session.cookie.path}")
    private String cookiePath;

    @Value("${app.session.cookie.http-only}")
    private boolean cookieHttpOnly;

    @Value("${app.session.cookie.expire-now}")
    private int cookieExpiredAge;

    public void addSessionCookie(HttpServletResponse response, SessionDto newUserSession) {
        Cookie newCookie = new Cookie(cookieName, newUserSession.getId().toString());
        newCookie.setPath(cookiePath);
        newCookie.setHttpOnly(cookieHttpOnly);

        response.addCookie(newCookie);
    }

    public void removeSessionCookie(Cookie foundCookie, HttpServletResponse response) {
        foundCookie.setPath(cookiePath);
        foundCookie.setMaxAge(cookieExpiredAge);

        response.addCookie(foundCookie);
    }

    public Optional<Cookie> findSessionCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst();
    }
}
