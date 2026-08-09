package com.zepic.attendance_platform.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentSummaryResponse {

    private Long id;
    private String name;
}
