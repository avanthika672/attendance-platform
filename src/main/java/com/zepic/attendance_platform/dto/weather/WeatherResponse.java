package com.zepic.attendance_platform.dto.weather;
import java.util.List;
import java.io.Serializable;

public record WeatherResponse (
        String name,
        MainWeather main,
        List<WeatherDescription> weather,
        Wind wind
)implements Serializable {
}
