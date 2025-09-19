package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.dto.WeatherDataDto;
import com.mysite.weatherviewer.dto.weather.OpenWeatherResponse;
import com.mysite.weatherviewer.mapper.WeatherDataMapper;
import com.mysite.weatherviewer.model.WeatherData;
import com.mysite.weatherviewer.repository.WeatherDataRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WeatherDataService {

    private final WeatherDataRepository weatherDataRepository;
    private final WeatherDataMapper weatherDataMapper;

    public void processWeatherData(Long locationId) {
        WeatherData wd = weatherDataRepository.findByLocationId(locationId);
    }

    @Transactional
    public void saveWeatherData(OpenWeatherResponse response, Long locationId) {
        WeatherDataDto weatherDataDto = weatherDataMapper.toWeatherDataDto(response, locationId);

        WeatherData newWeatherData = weatherDataMapper.toWeatherData(weatherDataDto);
        newWeatherData.setUpdatedAt(Instant.now());

        weatherDataRepository.save(newWeatherData);
    }
}
