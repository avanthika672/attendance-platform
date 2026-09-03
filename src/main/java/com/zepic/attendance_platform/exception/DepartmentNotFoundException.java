package com.zepic.attendance_platform.exception;

import org.springframework.http.HttpStatus;

public class DepartmentNotFoundException extends ApiException {
    public DepartmentNotFoundException() {
        super(
                "DEPARTMENT_NOT_FOUND", "Department was not found",
                HttpStatus.NOT_FOUND
        );
    }
}