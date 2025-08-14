package com.mysite.weatherviewer.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Weather {

    @JsonProperty("icon")
    private String iconId;
}
