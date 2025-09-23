package com.mysite.weatherviewer.exception;

public class WeatherServiceIsNotRespondingException extends RuntimeException {
    public WeatherServiceIsNotRespondingException(String message) {
        super(message);
    }
}
