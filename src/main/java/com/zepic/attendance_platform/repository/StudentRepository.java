package com.zepic.attendance_platform.repository;

import com.zepic.attendance_platform.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByIdAndCollege_Id(Long id, Long collegeId);

    Optional<Student> findByUser_Id(Long userId);

    Page<Student> findByCollege_Id(Long collegeId, Pageable pageable);

    @Query("""
            SELECT s
            FROM Student s
            WHERE s.college.id = :collegeId
              AND (
                    LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
                 OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
                 OR LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :search, '%'))
              )
            """)
    Page<Student> searchByCollegeId(
            @Param("collegeId") Long collegeId,
            @Param("search") String search,
            Pageable pageable
    );
    boolean existsByCollege_IdAndRollNumber(
            Long collegeId,
            String rollNumber
    );

}