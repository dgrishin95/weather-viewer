package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.dto.LocationDto;
import com.mysite.weatherviewer.dto.weather.OpenWeatherResponse;
import com.mysite.weatherviewer.mapper.LocationMapper;
import com.mysite.weatherviewer.model.Location;
import com.mysite.weatherviewer.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Transactional
    public LocationDto findByNameAndUserId(String name, Long userId) {
        Location foundLocation = locationRepository.findByNameAndUserId(name, userId);

        if (foundLocation == null) {
            return null;
        }

        return locationMapper.toLocationDto(foundLocation);
    }

    @Transactional
    public LocationDto saveLocation(OpenWeatherResponse response, Long userId) {
        LocationDto locationDto = locationMapper.toLocationDto(response, userId);
        Location newLocation = locationMapper.toLocation(locationDto);

        Location savedLocation = locationRepository.save(newLocation);

        return locationMapper.toLocationDto(savedLocation);
    }
}
