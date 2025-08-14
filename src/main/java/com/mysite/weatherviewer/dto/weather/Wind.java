package com.mysite.weatherviewer.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class Wind {

    @JsonProperty("speed")
    private BigDecimal windSpeed;
}
