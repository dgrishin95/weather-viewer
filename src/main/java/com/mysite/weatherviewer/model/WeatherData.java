package com.mysite.weatherviewer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "weather_data")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "temperature", nullable = false)
    private BigDecimal temperature;

    @Column(name = "feels_like", nullable = false)
    private BigDecimal feelsLike;

    @Column(name = "icon_id")
    private String iconId;

    @Column(name = "wind_speed", nullable = false)
    private BigDecimal windSpeed;

    @Column(name = "humidity", nullable = false)
    private Integer humidity;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false)
    private Location location;
}
