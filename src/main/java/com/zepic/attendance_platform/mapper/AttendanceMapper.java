package com.zepic.attendance_platform.mapper;

import com.zepic.attendance_platform.dto.request.MarkAttendanceRequest;
import com.zepic.attendance_platform.dto.response.AttendanceResponse;
import com.zepic.attendance_platform.dto.response.AttendanceStudentSummaryResponse;
import com.zepic.attendance_platform.dto.response.CourseSummaryResponse;
import com.zepic.attendance_platform.entity.AttendanceRecord;
import com.zepic.attendance_platform.entity.Course;
import com.zepic.attendance_platform.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserBriefMapper.class)
public interface AttendanceMapper {

    CourseSummaryResponse toCourseSummary(Course course);

    default AttendanceStudentSummaryResponse toStudentSummary(Student student) {
        if (student == null) {
            return null;
        }

        return AttendanceStudentSummaryResponse.builder()
                .id(student.getId())
                .rollNumber(student.getRollNumber())
                .displayName(student.getFirstName() + " " + student.getLastName())
                .build();
    }

    AttendanceResponse toResponse(AttendanceRecord entity);

    @Mapping(target = "course", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "college", ignore = true)
    @Mapping(target = "markedBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AttendanceRecord toEntity(MarkAttendanceRequest request);
}
