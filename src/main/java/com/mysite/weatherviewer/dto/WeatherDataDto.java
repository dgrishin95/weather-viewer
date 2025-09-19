package com.mysite.weatherviewer.dto;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDataDto {
    private Long locationId;
    private BigDecimal temperature;
    private BigDecimal feelsLike;
    private String iconId;
    private BigDecimal windSpeed;
    private Integer humidity;
    private Instant updatedAt;
}
