package com.mysite.weatherviewer.mapper;

import com.mysite.weatherviewer.dto.UserWeatherDto;
import com.mysite.weatherviewer.mapper.config.DefaultMapperConfig;
import com.mysite.weatherviewer.model.WeatherData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DefaultMapperConfig.class, uses = {LocationMapper.class, WeatherDataMapper.class})
public interface UserWeatherMapper {

    @Mapping(source = "weatherData.location", target = "location")
    @Mapping(source = "weatherData", target = "weatherData")
    UserWeatherDto toUserWeatherDto(WeatherData weatherData);
}
