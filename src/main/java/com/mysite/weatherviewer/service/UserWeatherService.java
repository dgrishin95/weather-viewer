package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.dto.UserWeatherDto;
import com.mysite.weatherviewer.mapper.WeatherDataMapper;
import com.mysite.weatherviewer.repository.WeatherDataRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserWeatherService {

    private final WeatherDataRepository weatherDataRepository;
    private final WeatherDataMapper weatherDataMapper;

    @Transactional
    public List<UserWeatherDto> findByUserId(Long userId) {
        return weatherDataRepository.findByUserId(userId)
                .stream()
                .map(weatherDataMapper::toUserWeatherDto)
                .toList();
    }
}
