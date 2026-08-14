package com.zepic.attendance_platform.exception;

public class StudentNotFoundException extends ApiException {

    public StudentNotFoundException() {
        super("STUDENT_NOT_FOUND", "Student was not found");
    }
}
