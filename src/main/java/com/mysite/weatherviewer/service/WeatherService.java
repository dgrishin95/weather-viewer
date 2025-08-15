package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.client.OpenWeatherClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final OpenWeatherClient openWeatherClient;
    private final LocationService locationService;

    public void searchByCityName(String cityName, Long userId) {
        if (locationService.isLocationExist(cityName, userId)) {
            // TODO: Если есть → решаем: обновляем данные прямо сейчас или возвращаем текущие
        } else {
            // TODO: Если нет → идём в OpenWeatherClient, получаем OpenWeatherResponse.

            // TODO: Из ответа маппером формируем Location и WeatherData.
            //Сохраняем всё в одной транзакции.
            //Возвращаем результат на страницу.
        }
    }
}
