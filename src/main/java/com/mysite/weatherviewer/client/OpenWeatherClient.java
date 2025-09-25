package com.mysite.weatherviewer.client;

import com.mysite.weatherviewer.dto.weather.OpenWeatherResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class OpenWeatherClient {

    private final RestTemplate restTemplate;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.url.saving}")
    private String urlForSaving;

    @Value("${weather.api.url.updating}")
    private String urlForUpdating;

    @Value("${weather.api.units}")
    private String apiUnits;

    @Value("${weather.api.appid}")
    private String apiAppId;

    public OpenWeatherResponse getResponseForSaving(String cityName) {
        URI url = UriComponentsBuilder
                .fromHttpUrl(apiUrl)
                .queryParam("q", cityName)
                .queryParam("appid", apiAppId)
                .queryParam("units", apiUnits)
                .build()
                .toUri();

        return restTemplate.getForObject(url, OpenWeatherResponse.class);
    }

    public OpenWeatherResponse getResponseForUpdating(String lon, String lat) {
        String url = String.format(urlForUpdating, apiUrl, lon, lat, apiAppId, apiUnits);
        return restTemplate.getForObject(url, OpenWeatherResponse.class);
    }
}
