package com.zepic.attendance_platform.repository;

import com.zepic.attendance_platform.entity.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {

    boolean existsByCourse_IdAndStudent_Id(Long courseId, Long studentId);

    long countByCourse_Id(Long courseId);

}
