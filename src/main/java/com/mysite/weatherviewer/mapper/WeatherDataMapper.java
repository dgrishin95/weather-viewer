package com.mysite.weatherviewer.mapper;

import com.mysite.weatherviewer.dto.WeatherDataDto;
import com.mysite.weatherviewer.dto.weather.OpenWeatherResponse;
import com.mysite.weatherviewer.mapper.config.DefaultMapperConfig;
import com.mysite.weatherviewer.model.WeatherData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DefaultMapperConfig.class, uses = {LocationMapper.class})
public interface WeatherDataMapper {

    @Mapping(source = "locationId", target = "locationId")
    @Mapping(source = "openWeatherResponse.main.temperature", target = "temperature")
    @Mapping(source = "openWeatherResponse.main.feelsLike", target = "feelsLike")
    @Mapping(target = "iconId", expression = "java( openWeatherResponse.getWeather().get(0).getIconId() )")
    @Mapping(source = "openWeatherResponse.wind.windSpeed", target = "windSpeed")
    @Mapping(source = "openWeatherResponse.main.humidity", target = "humidity")
    WeatherDataDto toWeatherDataDto(OpenWeatherResponse openWeatherResponse, Long locationId);

    @Mapping(source = "oldDataWeatherDataDto.id", target = "id")
    @Mapping(source = "oldDataWeatherDataDto.locationId", target = "locationId")
    @Mapping(source = "openWeatherResponse.main.temperature", target = "temperature")
    @Mapping(source = "openWeatherResponse.main.feelsLike", target = "feelsLike")
    @Mapping(target = "iconId", expression = "java( openWeatherResponse.getWeather().get(0).getIconId() )")
    @Mapping(source = "openWeatherResponse.wind.windSpeed", target = "windSpeed")
    @Mapping(source = "openWeatherResponse.main.humidity", target = "humidity")
    WeatherDataDto toWeatherDataDto(OpenWeatherResponse openWeatherResponse, WeatherDataDto oldDataWeatherDataDto);

    @Mapping(target = "location",
            expression = "java( new com.mysite.weatherviewer.model.Location(weatherDataDto.getLocationId()) )")
    WeatherData toWeatherData(WeatherDataDto weatherDataDto);

    @Mapping(source = "location.id", target = "locationId")
    WeatherDataDto toWeatherDataDto(WeatherData weatherData);
}
