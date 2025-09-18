package com.mysite.weatherviewer.mapper;

import com.mysite.weatherviewer.dto.WeatherDataDto;
import com.mysite.weatherviewer.dto.weather.OpenWeatherResponse;
import com.mysite.weatherviewer.mapper.config.DefaultMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DefaultMapperConfig.class)
public interface WeatherDataMapper {

    @Mapping(source = "openWeatherResponse.main.temperature", target = "temperature")
    @Mapping(source = "openWeatherResponse.main.feelsLike", target = "feelsLike")
    @Mapping(target = "iconId", expression = "java( openWeatherResponse.getWeather().get(0).getIconId() )")
    @Mapping(source = "openWeatherResponse.wind.windSpeed", target = "windSpeed")
    @Mapping(source = "openWeatherResponse.main.humidity", target = "humidity")
    WeatherDataDto toWeatherDataDto(OpenWeatherResponse openWeatherResponse);
}
