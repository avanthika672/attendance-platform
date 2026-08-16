package com.zepic.attendance_platform.mapper;
import com.zepic.attendance_platform.dto.response.DepartmentSummaryResponse;
import com.zepic.attendance_platform.entity.Department;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentSummaryResponse toSummaryResponse(Department department);
}