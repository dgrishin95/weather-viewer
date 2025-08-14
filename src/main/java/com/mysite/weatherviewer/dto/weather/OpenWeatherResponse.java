package com.mysite.weatherviewer.dto.weather;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenWeatherResponse {
    private Coord coord;

    private List<Weather> weather;

    private Main main;

    private Wind wind;

    private String name;
}
