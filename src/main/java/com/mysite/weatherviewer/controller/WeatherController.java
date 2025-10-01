package com.mysite.weatherviewer.controller;

import com.mysite.weatherviewer.common.RequestAttributeKeys;
import com.mysite.weatherviewer.dto.SessionDto;
import com.mysite.weatherviewer.dto.UserWeatherDto;
import com.mysite.weatherviewer.service.SearchWeatherService;
import com.mysite.weatherviewer.service.UserWeatherService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final SearchWeatherService searchWeatherService;
    private final UserWeatherService userWeatherService;

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request, Model model) {
        SessionDto foundSession = (SessionDto) request.getAttribute(RequestAttributeKeys.USER_SESSION);
        List<UserWeatherDto> userWeatherData = userWeatherService.getUserWeatherData(foundSession.getUserId());

        model.addAttribute(RequestAttributeKeys.USER_WEATHER_DATA, userWeatherData);

        return "weather/welcome";
    }

    @PostMapping("/searchByCityName")
    public String searchByCityName(HttpServletRequest request,
                                   @RequestParam("cityName") String cityName,
                                   RedirectAttributes redirectAttributes) {
        try {
            SessionDto foundSession = (SessionDto) request.getAttribute(RequestAttributeKeys.USER_SESSION);
            searchWeatherService.searchByCityName(cityName, foundSession.getUserId());
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/weather/welcome";
    }
}
