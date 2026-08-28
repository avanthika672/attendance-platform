package com.zepic.attendance_platform.dto;

public record LoginRequest(
        String email,
        String password
) { }