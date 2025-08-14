package com.mysite.weatherviewer.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class Main {

    @JsonProperty("temp")
    private BigDecimal temperature;

    @JsonProperty("feels_like")
    private BigDecimal feelsLike;

    @JsonProperty("humidity")
    private Integer humidity;
}
