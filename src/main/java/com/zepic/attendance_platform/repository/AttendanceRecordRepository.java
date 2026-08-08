package com.zepic.attendance_platform.repository;

import com.zepic.attendance_platform.entity.AttendanceRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    boolean existsByCourse_IdAndStudent_IdAndAttendanceDate(
            Long courseId,
            Long studentId,
            LocalDate attendanceDate
    );

    @Query("""
            SELECT a
            FROM AttendanceRecord a
            WHERE a.college.id = :collegeId
              AND a.course.id = :courseId
              AND (:attendanceDate IS NULL OR a.attendanceDate = :attendanceDate)
              AND (:studentId IS NULL OR a.student.id = :studentId)
              AND (:status IS NULL OR a.status = :status)
            """)
    Page<AttendanceRecord> findForTeacherListing(
            @Param("collegeId") Long collegeId,
            @Param("courseId") Long courseId,
            @Param("attendanceDate") LocalDate attendanceDate,
            @Param("studentId") Long studentId,
            @Param("status") String status,
            Pageable pageable
    );

    long countByCourse_IdAndAttendanceDate(
            Long courseId,
            LocalDate attendanceDate
    );

    long countByCourse_IdAndAttendanceDateAndStatus(
            Long courseId,
            LocalDate attendanceDate,
            String status
    );

    @Query("""
            SELECT a
            FROM AttendanceRecord a
            WHERE a.college.id = :collegeId
              AND a.student.id = :studentId
              AND (:courseId IS NULL OR a.course.id = :courseId)
              AND (:fromDate IS NULL OR a.attendanceDate >= :fromDate)
              AND (:toDate IS NULL OR a.attendanceDate <= :toDate)
            """)
    Page<AttendanceRecord> findForStudentHistory(
            @Param("collegeId") Long collegeId,
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable
    );

}