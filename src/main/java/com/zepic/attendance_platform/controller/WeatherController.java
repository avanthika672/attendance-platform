package com.zepic.attendance_platform.controller;

import com.zepic.attendance_platform.dto.weather.WeatherResponse;
import com.zepic.attendance_platform.service.WeatherService;
import com.zepic.attendance_platform.util.CityNormalizer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
    private final WeatherService weatherService;
    public WeatherController(WeatherService weatherService){
        this.weatherService=weatherService;
    }
    @GetMapping("/weather")
    public WeatherResponse getWeather(
            @RequestParam String city
    ){
        String normalizedCity = CityNormalizer.normalize(city);
        return weatherService.getWeather(normalizedCity);
    }
}

