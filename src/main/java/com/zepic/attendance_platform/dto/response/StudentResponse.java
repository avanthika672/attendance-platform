package com.zepic.attendance_platform.dto.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {

    private Long id;
    private String rollNumber;
    private String firstName;
    private String lastName;
    private String email;
    private DepartmentSummaryResponse department;
    private String status;
    private Instant createdAt;
}