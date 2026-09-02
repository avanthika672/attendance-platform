package com.zepic.attendance_platform.client;

import com.zepic.attendance_platform.dto.weather.WeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name="openWeatherClient",
        url="https://api.openweathermap.org"
)
public interface WeatherClient {
    @GetMapping("/data/2.5/weather")
    WeatherResponse getWeather(
            @RequestParam("q") String city,
            @RequestParam("appid") String apiKey,
            @RequestParam("units") String units
    );
}
