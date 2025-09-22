package com.mysite.weatherviewer.client;

import com.mysite.weatherviewer.dto.weather.OpenWeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OpenWeatherClient {

    private final RestTemplate restTemplate;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.units}")
    private String apiUnits;

    @Value("${weather.api.appid}")
    private String apiAppId;

    public OpenWeatherResponse getResponseForSaving(String cityName) {
        String url = String.format("%s?q=%s&appid=%s&units=%s", apiUrl, cityName, apiAppId, apiUnits);
        return restTemplate.getForObject(url, OpenWeatherResponse.class);
    }

    public OpenWeatherResponse getResponseForUpdating(String lon, String lat) {
        String url = String.format("%s?lon=%s&lat=%s&appid=%s&units=%s", apiUrl, lon, lat, apiAppId, apiUnits);
        return restTemplate.getForObject(url, OpenWeatherResponse.class);
    }
}
