package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional
    public boolean isLocationExist(String name, Long userId) {
        return locationRepository.isLocationExist(name, userId);
    }
}
