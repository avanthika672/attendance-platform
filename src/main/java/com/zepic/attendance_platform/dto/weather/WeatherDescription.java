package com.zepic.attendance_platform.dto.weather;

import java.io.Serializable;

public record WeatherDescription (
    String description
)implements Serializable {
}
