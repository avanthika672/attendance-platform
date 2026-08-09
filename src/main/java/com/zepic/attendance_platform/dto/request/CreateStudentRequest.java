package com.zepic.attendance_platform.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStudentRequest {

    private String rollNumber;
    private String firstName;
    private String lastName;
    private String email;
    private Long departmentId;
}