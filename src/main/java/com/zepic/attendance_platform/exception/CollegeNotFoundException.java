package com.zepic.attendance_platform.exception;

public class CollegeNotFoundException extends ApiException {

    public CollegeNotFoundException() {
        super("COLLEGE_NOT_FOUND", "College not found");
    }
}