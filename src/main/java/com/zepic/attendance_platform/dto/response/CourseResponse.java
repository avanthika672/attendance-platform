package com.zepic.attendance_platform.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {

    private Long id;
    private String code;
    private String name;
    private String semester;
    private UserBriefResponse teacher;
}
