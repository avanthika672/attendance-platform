package com.zepic.attendance_platform.repository;

import com.zepic.attendance_platform.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByIdAndCollege_Id(
            Long id,
            Long collegeId
    );

}