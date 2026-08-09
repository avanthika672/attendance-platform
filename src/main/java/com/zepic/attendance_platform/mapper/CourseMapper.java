package com.zepic.attendance_platform.mapper;

import com.zepic.attendance_platform.dto.response.CourseResponse;
import com.zepic.attendance_platform.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserBriefMapper.class)
public interface CourseMapper {

    CourseResponse toResponse(Course entity);
}
