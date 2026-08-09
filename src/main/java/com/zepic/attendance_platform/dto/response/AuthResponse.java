package com.zepic.attendance_platform.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private UserSummaryResponse user;
    private CollegeSummaryResponse college;
}