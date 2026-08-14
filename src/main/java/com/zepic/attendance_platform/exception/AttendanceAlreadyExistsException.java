package com.zepic.attendance_platform.exception;

public class AttendanceAlreadyExistsException extends ApiException {

    public AttendanceAlreadyExistsException() {
        super("ATTENDANCE_ALREADY_EXISTS", "Attendance has already been recorded for this student, course, and date");
    }
}