package com.mysite.weatherviewer.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private String name;
    private Long userId;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
