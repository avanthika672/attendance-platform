package com.zepic.attendance_platform.exception;

public class TeacherRoleRequiredException extends ApiException {

    public TeacherRoleRequiredException() {
        super("TEACHER_ROLE_REQUIRED", "This operation requires the TEACHER role");
    }
}
