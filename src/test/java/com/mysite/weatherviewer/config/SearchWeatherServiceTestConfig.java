package com.mysite.weatherviewer.config;

import com.mysite.weatherviewer.client.OpenWeatherClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
@Import(TestConfig.class)
public class SearchWeatherServiceTestConfig {

    @Bean
    @Primary
    public OpenWeatherClient openWeatherClient() {
        return Mockito.mock(OpenWeatherClient.class);
    }
}
