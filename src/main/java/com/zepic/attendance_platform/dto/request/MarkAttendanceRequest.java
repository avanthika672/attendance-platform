package com.zepic.attendance_platform.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkAttendanceRequest {

    private Long courseId;
    private Long studentId;
    private LocalDate attendanceDate;
    private String status;
    private String remarks;
}
