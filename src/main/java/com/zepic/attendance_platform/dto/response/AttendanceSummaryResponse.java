package com.zepic.attendance_platform.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceSummaryResponse {

    private CourseSummaryResponse course;
    private LocalDate date;
    private long totalStudents;
    private long present;
    private long absent;
    private long late;
    private long unmarked;
    private double attendancePercentage;
}