package com.zepic.attendance_platform.mapper;

import com.zepic.attendance_platform.dto.response.AuthResponse;
import com.zepic.attendance_platform.dto.response.CollegeSummaryResponse;
import com.zepic.attendance_platform.dto.response.UserSummaryResponse;
import com.zepic.attendance_platform.entity.AppUser;
import com.zepic.attendance_platform.entity.College;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    UserSummaryResponse toUserSummary(AppUser user);

    CollegeSummaryResponse toCollegeSummary(College college);

    default AuthResponse toAuthResponse(AppUser user) {
        if (user == null) {
            return null;
        }

        return AuthResponse.builder()
                .user(toUserSummary(user))
                .college(toCollegeSummary(user.getCollege()))
                .build();
    }
}
