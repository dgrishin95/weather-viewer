package com.mysite.weatherviewer.controller;

import com.mysite.weatherviewer.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @PostMapping("/get")
    public String test(@RequestParam("cityName") String cityName) {
        weatherService.test(cityName);

        return "redirect:/auth/welcome";
    }
}
