package com.mysite.weatherviewer.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestAttributeKeys {
    public static final String USER_SESSION = "USER_SESSION";
    public static final String USER_COOKIE = "USER_COOKIE";
    public static final String USER_WEATHER_DATA = "USER_WEATHER_DATA";
}
