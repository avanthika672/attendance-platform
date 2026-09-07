package com.zepic.attendance_platform.exception;

import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler =  new GlobalExceptionHandler();

    @Test
    void handleApiException_shouldReturnExceptionStatus(){
        ResponseEntity<Map<String, Object>> response =
                handler.handleApiException(new DepartmentNotFoundException());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("DEPARTMENT_NOT_FOUND", response.getBody().get("code"));
        assertEquals("Department was not found", response.getBody().get("message"));
    }

    @Test
    void handleCityNotFound_shouldReturn404(){
        FeignException.NotFound exception =
                (FeignException.NotFound) FeignException.errorStatus(
                        "weather",
                        Response.builder()
                                .status(404)
                                .reason("Not Found")
                                .request(Request.create(Request.HttpMethod.GET, "http://test",
                                        Map.of(),
                                        null,
                                        null,
                                        null))
                                .build());

        ResponseEntity<Map<String, Object>> response =
                handler.handleCityNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("CITY_NOT_FOUND", response.getBody().get("code"));
        assertEquals("City not found", response.getBody().get("message"));
    }

    @Test
    void handleWeatherAuthenticationError_shouldReturn401(){
        FeignException.Unauthorized exception =
                (FeignException.Unauthorized) FeignException.errorStatus(
                        "weather",
                        Response.builder()
                                .status(401)
                                .reason("Unauthorized")
                                .request(Request.create(Request.HttpMethod.GET, "http://test",
                                        Map.of(),
                                        null,null,null))
                                .build());

        ResponseEntity<Map<String,Object>> response =
                handler.handleWeatherAuthenticationError(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("WEATHER_SERVICE_AUTH_ERROR", response.getBody().get("code"));
        assertEquals("Weather service authentication failed", response.getBody().get("message"));
    }

    @Test
    void handleWeatherServiceError_shouldReturn502(){
        FeignException exception = FeignException.errorStatus("weather", Response.builder()
                .status(502)
                .reason("Internal server error")
                .request(Request.create(Request.HttpMethod.GET, "http://test",
                        Map.of(), null,null,null))
                .build());

        ResponseEntity<Map<String,Object>> response = handler.handleWeatherServiceError(exception);

        assertEquals(HttpStatus.BAD_GATEWAY,response.getStatusCode());
        assertEquals("WEATHER_SERVICE_ERROR", response.getBody().get("code"));
        assertEquals("Weather service is unavailable", response.getBody().get("message"));
    }

    @Test
    void handleAccessDenied_shouldReturn403(){
        ResponseEntity<Map<String,Object>> response = handler.handleAccessDenied(new AccessDeniedException("denied"));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("FORBIDDEN", response.getBody().get("code"));
    }

}
