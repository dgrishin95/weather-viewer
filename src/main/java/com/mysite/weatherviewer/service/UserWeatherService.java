package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.dto.UserWeatherDto;
import com.mysite.weatherviewer.dto.WeatherDataDto;
import com.mysite.weatherviewer.mapper.UserWeatherMapper;
import com.mysite.weatherviewer.repository.UserWeatherRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserWeatherService {

    private final UserWeatherRepository userWeatherRepository;
    private final UserWeatherMapper userWeatherMapper;
    private final WeatherService weatherService;

    @Transactional
    public List<UserWeatherDto> getUserWeatherData(Long userId) {
        List<UserWeatherDto> userWeatherData = userWeatherRepository.findByUserId(userId)
                .stream()
                .map(userWeatherMapper::toUserWeatherDto)
                .toList();

        List<UserWeatherDto> list = userWeatherData
                .stream()
                .map(userWeatherDto ->
                        isNeededUpdate(userWeatherDto) ? updateData(userWeatherDto) : userWeatherDto)
                .toList();

        return null;
    }

    private boolean isNeededUpdate(UserWeatherDto userWeatherDto) {
        return true;
    }

    private UserWeatherDto updateData(UserWeatherDto userWeatherDto) {
        WeatherDataDto updatedWeatherDataDto =
                weatherService.updateData(userWeatherDto.getLocation());

        userWeatherDto.setWeatherData(updatedWeatherDataDto);

        return userWeatherDto;
    }
}
