package com.mysite.weatherviewer.integration;

import com.mysite.weatherviewer.client.OpenWeatherClient;
import com.mysite.weatherviewer.client.OpenWeatherStatusCode;
import com.mysite.weatherviewer.config.SearchWeatherServiceTestConfig;
import com.mysite.weatherviewer.dto.LocationDto;
import com.mysite.weatherviewer.dto.RegisterDto;
import com.mysite.weatherviewer.dto.UserDto;
import com.mysite.weatherviewer.dto.weather.Coord;
import com.mysite.weatherviewer.dto.weather.Main;
import com.mysite.weatherviewer.dto.weather.OpenWeatherResponse;
import com.mysite.weatherviewer.dto.weather.Weather;
import com.mysite.weatherviewer.dto.weather.Wind;
import com.mysite.weatherviewer.exception.CityNotFoundException;
import com.mysite.weatherviewer.exception.InvalidApiKeyException;
import com.mysite.weatherviewer.exception.RequestLimitExceededException;
import com.mysite.weatherviewer.model.Location;
import com.mysite.weatherviewer.model.WeatherData;
import com.mysite.weatherviewer.repository.LocationRepository;
import com.mysite.weatherviewer.repository.WeatherDataRepository;
import com.mysite.weatherviewer.service.LocationService;
import com.mysite.weatherviewer.service.SearchWeatherService;
import com.mysite.weatherviewer.service.UserService;
import com.mysite.weatherviewer.service.WeatherDataService;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(SearchWeatherServiceTestConfig.class)
@Transactional
class SearchWeatherServiceIntegrationTest {

    @Autowired
    private SearchWeatherService searchWeatherService;

    @Autowired
    private OpenWeatherClient openWeatherClient;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherDataService weatherDataService;

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Value("${app.weather.cache.duration.minutes}")
    private long weatherCacheDurationMinutes;

    @BeforeEach
    void setUp() {
        Mockito.reset(openWeatherClient);
    }

    private static final String TEST_LOGIN = "testuser";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_CITY_NAME = "Moscow";
    private static final String TEST_LONGITUDE = "37.6156";
    private static final String TEST_LATITUDE = "55.7522";

    @Test
    void testSearch_SuccessfulCreate() {
        when(openWeatherClient.getResponseForSaving(TEST_CITY_NAME))
                .thenReturn(createSuccessfulMockResponse());

        UserDto userDto = registerUser();
        searchWeatherService.searchByCityName(TEST_CITY_NAME, userDto.getId());
        Location location = locationRepository.findByNameAndUserId(TEST_CITY_NAME, userDto.getId());

        verify(openWeatherClient).getResponseForSaving(TEST_CITY_NAME);
        assertNotNull(location);
        assertEquals(TEST_CITY_NAME, location.getName());
        assertEquals(location.getLatitude(), createSuccessfulMockResponse().getCoord().getLatitude());
    }

    @Test
    void testSearch_SuccessfulUpdate() {
        when(openWeatherClient.getResponseForUpdating(TEST_LONGITUDE, TEST_LATITUDE))
                .thenReturn(createSuccessfulMockResponse());

        UserDto userDto = registerUser();
        LocationDto locationDto = locationService.saveLocation(createSuccessfulMockResponse(), userDto.getId());
        weatherDataService.createWeatherData(createSuccessfulMockResponse(), locationDto.getId());

        WeatherData weatherDataBeforeUpdate = weatherDataRepository.findByLocationId(locationDto.getId());
        Instant oldUpdatedAt = weatherDataBeforeUpdate.getUpdatedAt();
        weatherDataBeforeUpdate.setUpdatedAt(oldUpdatedAt.minus(Duration.ofMinutes(weatherCacheDurationMinutes)));

        searchWeatherService.searchByCityName(TEST_CITY_NAME, userDto.getId());

        WeatherData weatherDataAfterUpdate = weatherDataRepository.findByLocationId(locationDto.getId());

        verify(openWeatherClient).getResponseForUpdating(TEST_LONGITUDE, TEST_LATITUDE);
        assertEquals(weatherDataAfterUpdate.getId(), weatherDataBeforeUpdate.getId());
        assertTrue(oldUpdatedAt.isBefore(weatherDataAfterUpdate.getUpdatedAt()));
    }

    @Test
    void testSearch_InvalidApiKey() {
        when(openWeatherClient.getResponseForSaving(TEST_CITY_NAME))
                .thenReturn(createInvalidApiKeyMockResponse(OpenWeatherStatusCode.INVALID_API_KEY.getCod()));

        assertThrows(InvalidApiKeyException.class,
                () -> searchWeatherService.searchByCityName(TEST_CITY_NAME, 1L));
    }

    @Test
    void testSearch_CityNotFound() {
        when(openWeatherClient.getResponseForSaving(TEST_CITY_NAME))
                .thenReturn(createInvalidApiKeyMockResponse(OpenWeatherStatusCode.CITY_NOT_FOUND.getCod()));

        assertThrows(CityNotFoundException.class,
                () -> searchWeatherService.searchByCityName(TEST_CITY_NAME, 1L));
    }

    @Test
    void testSearch_LimitExceeded() {
        when(openWeatherClient.getResponseForSaving(TEST_CITY_NAME))
                .thenReturn(createInvalidApiKeyMockResponse(OpenWeatherStatusCode.LIMIT_EXCEEDED.getCod()));

        assertThrows(RequestLimitExceededException.class,
                () -> searchWeatherService.searchByCityName(TEST_CITY_NAME, 1L));
    }

    private OpenWeatherResponse createSuccessfulMockResponse() {
        return OpenWeatherResponse
                .builder()
                .coord(Coord
                        .builder()
                        .longitude(new BigDecimal(TEST_LONGITUDE))
                        .latitude(new BigDecimal(TEST_LATITUDE))
                        .build())
                .weather(List.of(Weather
                        .builder()
                        .iconId("04d")
                        .build()))
                .main(Main
                        .builder()
                        .temperature(new BigDecimal("11.4"))
                        .feelsLike(new BigDecimal("10.61"))
                        .humidity(77)
                        .build())
                .wind(Wind
                        .builder()
                        .windSpeed(new BigDecimal("1.32"))
                        .build())
                .name(TEST_CITY_NAME)
                .cod(200)
                .build();
    }

    private OpenWeatherResponse createInvalidApiKeyMockResponse(Integer cod) {
        return OpenWeatherResponse
                .builder()
                .cod(cod)
                .build();
    }

    private UserDto registerUser() {
        RegisterDto registerDto = new RegisterDto(TEST_LOGIN, TEST_PASSWORD);
        return userService.register(registerDto);
    }
}
