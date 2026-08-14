package com.zepic.attendance_platform.mapper;
import com.zepic.attendance_platform.entity.College;
import org.mapstruct.Mapper;
import com.zepic.attendance_platform.dto.response.CollegeSummaryResponse;

@Mapper(componentModel = "spring")
public interface CollegeMapper {
    CollegeSummaryResponse toSummaryResponse(College college);

}