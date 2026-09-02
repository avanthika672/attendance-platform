package com.zepic.attendance_platform.service;

import com.zepic.attendance_platform.client.WeatherClient;
import com.zepic.attendance_platform.dto.weather.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public WeatherResponse getWeather(String city){
        return weatherClient.getWeather(city,apiKey,"metric");
    }
}
