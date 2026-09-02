package com.zepic.attendance_platform.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record MainWeather(
    double temp,
    @JsonProperty("feels_like")
    double feelsLike,
    int humidity
)implements Serializable {
}
