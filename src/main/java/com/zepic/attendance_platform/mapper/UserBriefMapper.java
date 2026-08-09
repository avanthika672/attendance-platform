package com.zepic.attendance_platform.mapper;

import com.zepic.attendance_platform.dto.response.UserBriefResponse;
import com.zepic.attendance_platform.entity.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserBriefMapper {

    UserBriefResponse toUserBrief(AppUser user);
}