package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.dto.UserWeatherDto;
import com.mysite.weatherviewer.dto.WeatherDataDto;
import com.mysite.weatherviewer.mapper.UserWeatherMapper;
import com.mysite.weatherviewer.repository.UserWeatherRepository;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserWeatherService {

    private final UserWeatherRepository userWeatherRepository;
    private final UserWeatherMapper userWeatherMapper;
    private final SearchWeatherService searchWeatherService;
    private final LocationService locationService;
    private final WeatherDataService weatherDataService;

    @Transactional
    public List<UserWeatherDto> getUserWeatherData(Long userId) {
        List<UserWeatherDto> userWeatherData = userWeatherRepository.findByUserId(userId)
                .stream()
                .map(userWeatherMapper::toUserWeatherDto)
                .toList();

        return userWeatherData
                .stream()
                .map(this::refreshWeatherIfNeeded)
                .sorted(Comparator.comparing(
                                (UserWeatherDto userWeatherDto) -> userWeatherDto.getLocation().getId())
                        .reversed()
                )
                .toList();
    }

    private UserWeatherDto refreshWeatherIfNeeded(UserWeatherDto userWeatherDto) {
        WeatherDataDto updatedWeatherDataDto =
                searchWeatherService.updateByLocationAndWeatherData(
                        userWeatherDto.getLocation(), userWeatherDto.getWeatherData());

        userWeatherDto.setWeatherData(updatedWeatherDataDto);

        return userWeatherDto;
    }

    @Transactional
    public void removeWeatherDataAndLocation(Long weatherDataId, Long locationId) {
        weatherDataService.remove(weatherDataId);
        locationService.remove(locationId);
    }
}
