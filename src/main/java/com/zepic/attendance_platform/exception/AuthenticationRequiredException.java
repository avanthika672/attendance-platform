package com.zepic.attendance_platform.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationRequiredException extends ApiException {

    public AuthenticationRequiredException() {
        super(
                "AUTHENTICATION_REQUIRED", "Authentication is required",
                HttpStatus.UNAUTHORIZED
        );
    }
}