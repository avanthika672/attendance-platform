package com.zepic.attendance_platform.exception;
public class DepartmentNotFoundException extends ApiException {
    public DepartmentNotFoundException() {
        super("DEPARTMENT_NOT_FOUND", "Department was not found");
    }
}