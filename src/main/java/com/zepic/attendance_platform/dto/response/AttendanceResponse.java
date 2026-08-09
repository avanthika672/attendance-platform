package com.zepic.attendance_platform.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponse {

    private Long id;
    private AttendanceStudentSummaryResponse student;
    private CourseSummaryResponse course;
    private LocalDate attendanceDate;
    private String status;
    private String remarks;
    private UserBriefResponse markedBy;
}
