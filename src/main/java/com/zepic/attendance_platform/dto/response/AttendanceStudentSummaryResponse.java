package com.zepic.attendance_platform.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceStudentSummaryResponse {

    private Long id;
    private String rollNumber;
    private String displayName;
}
