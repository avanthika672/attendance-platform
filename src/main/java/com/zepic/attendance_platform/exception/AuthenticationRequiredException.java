package com.zepic.attendance_platform.exception;

public class AuthenticationRequiredException extends ApiException {

    public AuthenticationRequiredException() {
        super("AUTHENTICATION_REQUIRED", "Authentication is required");
    }
}