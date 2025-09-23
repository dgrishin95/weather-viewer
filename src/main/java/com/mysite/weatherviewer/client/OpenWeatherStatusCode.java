package com.mysite.weatherviewer.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OpenWeatherStatusCode {
    OK(200),
    INVALID_API_KEY(401),
    CITY_NOT_FOUND(404),
    LIMIT_EXCEEDED(429);

    private final int cod;

    public static OpenWeatherStatusCode fromCode(int code) {
        for (OpenWeatherStatusCode statusCode : values()) {
            if (statusCode.getCod() == code) {
                return statusCode;
            }
        }

        return null;
    }
}
