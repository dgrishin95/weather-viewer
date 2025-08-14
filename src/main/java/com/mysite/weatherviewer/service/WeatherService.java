package com.mysite.weatherviewer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final OpenWeatherClient openWeatherClient;

    public void test(String cityName) {
        openWeatherClient.getResponse(cityName);
    }
}
