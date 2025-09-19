package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.client.OpenWeatherClient;
import com.mysite.weatherviewer.dto.LocationDto;
import com.mysite.weatherviewer.dto.weather.OpenWeatherResponse;
import com.mysite.weatherviewer.model.Location;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final OpenWeatherClient openWeatherClient;
    private final LocationService locationService;
    private final WeatherDataService weatherDataService;

    @Transactional
    public void searchByCityName(String cityName, Long userId) {
        Optional<Location> foundLocation = locationService.findByNameAndUserId(cityName, userId);

        if (foundLocation.isPresent()) {
            // TODO: Если есть → решаем: обновляем данные прямо сейчас или возвращаем текущие
//            weatherDataService.processWeatherData(foundLocation.get().getId());
        } else {
            OpenWeatherResponse response = openWeatherClient.getResponse(cityName);
            parseResponse(response, userId);
        }
    }

    private void parseResponse(OpenWeatherResponse response, Long userId) {
        LocationDto savedLocationDto = locationService.saveLocation(response, userId);
        weatherDataService.saveWeatherData(response, savedLocationDto.getId());
    }
}
