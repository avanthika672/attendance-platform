package com.zepic.attendance_platform.exception;
import feign.FeignException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException exception) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "code", exception.getCode(),
                "message", exception.getMessage()
        );
        return ResponseEntity
                .status(exception.getStatus())
                .body(body);
    }
    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Map<String,Object>> handleCityNotFound(
            FeignException.NotFound exception){
        Map<String,Object> body= Map.of(
                "timestamp",Instant.now().toString(),
                "code","CITY_NOT_FOUND",
                "message", "City not found"
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }
    @ExceptionHandler(FeignException.Unauthorized.class)
    public ResponseEntity<Map<String,Object>> handleWeatherAuthenticationError(
            FeignException.Unauthorized exception){
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "code", "WEATHER_SERVICE_AUTH_ERROR",
                "message", "Weather service authentication failed"
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String,Object>> handleWeatherServiceError(
            FeignException exception){
        Map<String,Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "code","WEATHER_SERVICE_ERROR",
                "message","Weather service is unavailable"
        );
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(body);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(
            AccessDeniedException exception) {

        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "code", "FORBIDDEN",
                "message", "You do not have permission to access this resource"
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(body);
    }





    }





