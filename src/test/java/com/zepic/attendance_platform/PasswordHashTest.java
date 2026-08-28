package com.zepic.attendance_platform;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
class PasswordHashTest {
    @Test
    void generateHash() {
        System.out.println(
                new BCryptPasswordEncoder().encode("password123")
        );
    }
}