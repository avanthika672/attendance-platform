package com.zepic.attendance_platform.repository;

import com.zepic.attendance_platform.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByIdAndCollege_Id(Long id, Long collegeId);

    Optional<Course> findByIdAndCollege_IdAndTeacher_Id(
            Long id,
            Long collegeId,
            Long teacherId
    );

    Page<Course> findByCollege_IdAndCourseEnrollments_Student_Id(
            Long collegeId,
            Long studentId,
            Pageable pageable
    );
}