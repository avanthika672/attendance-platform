package com.zepic.attendance_platform.service;

import com.zepic.attendance_platform.client.WeatherClient;
import com.zepic.attendance_platform.dto.weather.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WeatherService{
    private final WeatherClient weatherClient;
    private final String apiKey;

    public WeatherService(
            WeatherClient weatherClient,
            @Value("${openweather.api.key}") String apiKey
    ){
        this.weatherClient=weatherClient;
        this.apiKey=apiKey;
    }
    @Cacheable(value="weather",key="#city")
    public WeatherResponse getWeather(String city){
        log.info("Going to hit API for city {}", city);
        return weatherClient.getWeather(city,apiKey,"metric");
    }
}
