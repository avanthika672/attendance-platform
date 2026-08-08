package com.zepic.attendance_platform.repository;

import com.zepic.attendance_platform.entity.LoginSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface LoginSessionRepository extends JpaRepository<LoginSession, String> {

    Optional<LoginSession> findByIdAndExpiresAtAfter(
            String id,
            Instant now
    );

    List<LoginSession> findByExpiresAtBefore(
            Instant now
    );

}
