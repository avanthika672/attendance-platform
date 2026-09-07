package com.zepic.attendance_platform.service;

import com.zepic.attendance_platform.client.WeatherClient;
import com.zepic.attendance_platform.dto.weather.WeatherResponse;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherServiceWireMockTest {
     static WireMockServer wireMock = new WireMockServer(0);
     static WeatherService weatherService;

     @BeforeAll
     static void setup(){
         wireMock.start();
         WeatherClient client = Feign.builder()
                 .contract(new SpringMvcContract())
                 .decoder(new JacksonDecoder())
                 .target(WeatherClient.class,wireMock.baseUrl());
         weatherService = new WeatherService(client, "test-api-key");
     }

     @AfterAll
     static void cleanup(){
         wireMock.stop();
     }

     @Test
     void getWeather_shouldReturnWeatherFromApi(){
         wireMock.stubFor(get(urlPathEqualTo("/data/2.5/weather"))
                 .withQueryParam("q", equalTo("Chennai"))
                 .withQueryParam("appid", equalTo("test-api-key"))
                 .withQueryParam("units",equalTo("metric"))
                 .willReturn(okJson("""
                        {
                          "name": "Chennai",
                          "main": {
                            "temp": 30.5
                          },
                          "weather": [],
                          "wind": {
                            "speed": 4.2
                          }
                        }
                        """)));
         WeatherResponse response = weatherService.getWeather("Chennai");
         assertEquals("Chennai",response.name());
         wireMock.verify(getRequestedFor(urlPathEqualTo("/data/2.5/weather"))
                 .withQueryParam("q", equalTo("Chennai"))
                 .withQueryParam("appid", equalTo("test-api-key"))
                 .withQueryParam("units",equalTo("metric")));

     }

}
