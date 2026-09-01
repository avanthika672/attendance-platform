package com.zepic.attendance_platform.dto.response;
import java.io.Serializable;
public record DepartmentSummaryResponse (
        Long id,
        String name
)implements Serializable{}
