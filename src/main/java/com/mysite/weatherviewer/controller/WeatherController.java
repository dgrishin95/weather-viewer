package com.mysite.weatherviewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    @PostMapping("/get")
    public String test(@RequestParam("city") String query) {
        return "redirect:/auth/welcome";
    }
}
