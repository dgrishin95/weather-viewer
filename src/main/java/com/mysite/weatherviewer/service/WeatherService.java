package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.client.OpenWeatherClient;
import com.mysite.weatherviewer.dto.LocationDto;
import com.mysite.weatherviewer.dto.WeatherDataDto;
import com.mysite.weatherviewer.dto.weather.OpenWeatherResponse;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final OpenWeatherClient openWeatherClient;
    private final LocationService locationService;
    private final WeatherDataService weatherDataService;

    @Value("${app.weather.cache.duration.minutes}")
    private long weatherCacheDurationMinutes;

    @Transactional
    public void searchByCityName(String cityName, Long userId) {
        LocationDto foundLocation = locationService.findByNameAndUserId(cityName, userId);

        if (foundLocation != null) {
            updateData(foundLocation);
        } else {
            saveData(cityName, userId);
        }
    }

    private void saveData(String cityName, Long userId) {
        OpenWeatherResponse response = openWeatherClient.getResponseForSaving(cityName);

        LocationDto savedLocation = locationService.saveLocation(response, userId);
        weatherDataService.saveWeatherData(response, savedLocation.getId());
    }

    private void updateData(LocationDto foundLocation) {
        WeatherDataDto foundWeatherData = weatherDataService.findByLocationId(foundLocation.getId());
        Instant updatedAt = foundWeatherData.getUpdatedAt();
        Instant now = Instant.now();

        if (updatedAt.plus(Duration.ofMinutes(weatherCacheDurationMinutes)).isBefore(now)) {
            String longitude = foundLocation.getLongitude().toString();
            String latitude = foundLocation.getLatitude().toString();

            OpenWeatherResponse response = openWeatherClient.getResponseForUpdating(longitude, latitude);

            weatherDataService.updateWeatherData(response, foundWeatherData);
        }
    }
}
