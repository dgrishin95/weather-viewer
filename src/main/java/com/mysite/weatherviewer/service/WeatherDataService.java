package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.dto.WeatherDataDto;
import com.mysite.weatherviewer.dto.weather.OpenWeatherResponse;
import com.mysite.weatherviewer.mapper.WeatherDataMapper;
import com.mysite.weatherviewer.model.WeatherData;
import com.mysite.weatherviewer.repository.WeatherDataRepository;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WeatherDataService {

    private final WeatherDataRepository weatherDataRepository;
    private final WeatherDataMapper weatherDataMapper;

    @Value("${app.weather.cache.duration.minutes}")
    private long weatherCacheDurationMinutes;

    public WeatherDataDto findByLocationId(Long locationId) {
        WeatherData weatherData = weatherDataRepository.findByLocationId(locationId);
        return weatherDataMapper.toWeatherDataDto(weatherData);
    }

    @Transactional
    public void createWeatherData(OpenWeatherResponse response, Long locationId) {
        WeatherDataDto newWeatherData = weatherDataMapper.toWeatherDataDto(response, locationId);
        saveOrUpdate(newWeatherData);
    }

    @Transactional
    public WeatherDataDto updateWeatherData(OpenWeatherResponse response, WeatherDataDto foundWeatherData) {
        foundWeatherData = weatherDataMapper.toWeatherDataDto(response, foundWeatherData);
        return saveOrUpdate(foundWeatherData);
    }

    private WeatherDataDto saveOrUpdate(WeatherDataDto weatherDataDto) {
        WeatherData weatherData = weatherDataMapper.toWeatherData(weatherDataDto);
        weatherData.setUpdatedAt(Instant.now());

        WeatherData savedWeatherData = weatherDataRepository.save(weatherData);

        return weatherDataMapper.toWeatherDataDto(savedWeatherData);
    }

    public boolean isWeatherDataNeededUpdate(WeatherDataDto foundWeatherData) {
        Instant updatedAt = foundWeatherData.getUpdatedAt();
        Instant now = Instant.now();

        return updatedAt.plus(Duration.ofMinutes(weatherCacheDurationMinutes)).isBefore(now);
    }
}
