package com.zepic.attendance_platform.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBriefResponse {

    private Long id;
    private String displayName;
}
