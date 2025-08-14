package com.mysite.weatherviewer.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class Coord {

    @JsonProperty("lat")
    private BigDecimal latitude;

    @JsonProperty("lon")
    private BigDecimal longitude;
}
