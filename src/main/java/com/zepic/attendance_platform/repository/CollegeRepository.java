package com.zepic.attendance_platform.repository;

import com.zepic.attendance_platform.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository extends JpaRepository<College, Long> {
}