package com.zepic.attendance_platform.exception;
public class StudentEmailAlreadyExistsException extends ApiException {
    public StudentEmailAlreadyExistsException() {
        super("STUDENT_EMAIL_ALREADY_EXISTS", "A student with this email already exists");
    }
}