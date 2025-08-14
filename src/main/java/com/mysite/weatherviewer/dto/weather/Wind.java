package com.mysite.weatherviewer.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class Wind {

    @JsonProperty("speed")
    private BigDecimal windSpeed;
}
