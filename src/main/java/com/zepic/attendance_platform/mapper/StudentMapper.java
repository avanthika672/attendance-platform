package com.zepic.attendance_platform.mapper;

import com.zepic.attendance_platform.dto.request.CreateStudentRequest;
import com.zepic.attendance_platform.dto.response.DepartmentSummaryResponse;
import com.zepic.attendance_platform.dto.response.StudentResponse;
import com.zepic.attendance_platform.entity.Department;
import com.zepic.attendance_platform.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    DepartmentSummaryResponse toDepartmentSummary(Department department);

    @Mapping(source = "user.email", target = "email")
    StudentResponse toResponse(Student entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "college", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "courseEnrollments", ignore = true)
    @Mapping(target = "attendanceRecords", ignore = true)
    Student toEntity(CreateStudentRequest request);
}
