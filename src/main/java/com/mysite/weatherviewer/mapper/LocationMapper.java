package com.mysite.weatherviewer.mapper;

import com.mysite.weatherviewer.dto.LocationDto;
import com.mysite.weatherviewer.dto.weather.OpenWeatherResponse;
import com.mysite.weatherviewer.mapper.config.DefaultMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DefaultMapperConfig.class)
public interface LocationMapper {

    @Mapping(source = "openWeatherResponse.name", target = "name")
    @Mapping(source = "openWeatherResponse.coord.latitude", target = "latitude")
    @Mapping(source = "openWeatherResponse.coord.longitude", target = "longitude")
    @Mapping(source = "userId", target = "userId")
    LocationDto toLocationDto(OpenWeatherResponse openWeatherResponse, Long userId);
}
