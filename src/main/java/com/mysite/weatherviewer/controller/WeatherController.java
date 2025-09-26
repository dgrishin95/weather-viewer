package com.mysite.weatherviewer.controller;

import com.mysite.weatherviewer.dto.SessionDto;
import com.mysite.weatherviewer.service.CookieService;
import com.mysite.weatherviewer.service.SessionService;
import com.mysite.weatherviewer.service.WeatherService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final CookieService cookieService;
    private final SessionService sessionService;

    @GetMapping("/welcome")
    public String welcome() {
        return "weather/welcome";
    }

    @PostMapping("/searchByCityName")
    public String searchByCityName(HttpServletRequest request,
                                   @RequestParam("cityName") String cityName,
                                   RedirectAttributes redirectAttributes) {
        try {
            Optional<Cookie> foundCookie = cookieService.findSessionCookie(request);

            foundCookie.ifPresent(cookie -> {
                SessionDto foundSession = sessionService.findByUuid(cookie.getValue());
                weatherService.searchByCityName(cityName, foundSession.getUserId());
            });
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/weather/welcome";
    }
}
