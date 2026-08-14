package com.zepic.attendance_platform.exception;

public class InvalidAttendanceStatusException extends ApiException {

    public InvalidAttendanceStatusException() {
        super("INVALID_ATTENDANCE_STATUS", "Status must be one of PRESENT, ABSENT, or LATE");
    }
}