package com.zepic.attendance_platform.security;

public record AuthenticatedUser(Long userId, Long collegeId, String role) {
}