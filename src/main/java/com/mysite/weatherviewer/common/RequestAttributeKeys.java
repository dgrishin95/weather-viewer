package com.mysite.weatherviewer.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestAttributeKeys {
    public static final String USER_SESSION = "userSession";
    public static final String USER_COOKIE = "userCookie";
    public static final String USER_WEATHER_DATA = "userWeatherData";
    public static final String USER = "user";
}
