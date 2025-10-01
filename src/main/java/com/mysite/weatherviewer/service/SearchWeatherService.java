package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.client.OpenWeatherClient;
import com.mysite.weatherviewer.client.OpenWeatherStatusCode;
import com.mysite.weatherviewer.dto.LocationDto;
import com.mysite.weatherviewer.dto.WeatherDataDto;
import com.mysite.weatherviewer.dto.weather.OpenWeatherResponse;
import com.mysite.weatherviewer.exception.CityNotFoundException;
import com.mysite.weatherviewer.exception.InvalidApiKeyException;
import com.mysite.weatherviewer.exception.InvalidCityNameException;
import com.mysite.weatherviewer.exception.RequestLimitExceededException;
import com.mysite.weatherviewer.exception.WeatherServiceIsNotRespondingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchWeatherService {

    private final OpenWeatherClient openWeatherClient;
    private final LocationService locationService;
    private final WeatherDataService weatherDataService;

    public final static String CITY_NAME_PATTERN = "^[A-Za-z]+(?:[ -][A-Za-z]+)*$";

    @Transactional
    public void searchByCityName(String cityName, Long userId) {
        validateCityName(cityName);
        LocationDto foundLocation = locationService.findByNameAndUserId(cityName, userId);

        if (foundLocation != null) {
            updateByLocation(foundLocation);
        } else {
            createLocation(cityName, userId);
        }
    }

    private void createLocation(String cityName, Long userId) {
        OpenWeatherResponse response = openWeatherClient.getResponseForSaving(cityName);
        validateResponse(response);

        LocationDto savedLocation = locationService.saveLocation(response, userId);
        weatherDataService.createWeatherData(response, savedLocation.getId());
    }

    private void updateByLocation(LocationDto foundLocation) {
        updateByLocationAndWeatherData(foundLocation, null);
    }

    public WeatherDataDto updateByLocationAndWeatherData(LocationDto foundLocation, WeatherDataDto foundWeatherData) {
        if (foundWeatherData == null) {
            foundWeatherData = weatherDataService.findByLocationId(foundLocation.getId());
        }

        if (weatherDataService.isWeatherDataNeededUpdate(foundWeatherData)) {
            String longitude = foundLocation.getLongitude().toString();
            String latitude = foundLocation.getLatitude().toString();

            OpenWeatherResponse response = openWeatherClient.getResponseForUpdating(longitude, latitude);
            validateResponse(response);

            foundWeatherData = weatherDataService.updateWeatherData(response, foundWeatherData);
        }

        return foundWeatherData;
    }

    private void validateResponse(OpenWeatherResponse response) {
        OpenWeatherStatusCode statusCode = OpenWeatherStatusCode.fromCode(response.getCod());

        switch (statusCode) {
            case OK -> {
            }
            case INVALID_API_KEY -> throw new InvalidApiKeyException("Invalid API key");
            case CITY_NOT_FOUND -> throw new CityNotFoundException("City not found");
            case LIMIT_EXCEEDED -> throw new RequestLimitExceededException("Request limit exceeded");
            default -> throw new WeatherServiceIsNotRespondingException("The weather service is not responding");
        }
    }

    private void validateCityName(String cityName) {
        if (!cityName.trim().matches(CITY_NAME_PATTERN)) {
            throw new InvalidCityNameException("Please enter the city name using Latin letters only. " +
                    "Example: 'New York' or 'Los-Angeles");
        }
    }
}
