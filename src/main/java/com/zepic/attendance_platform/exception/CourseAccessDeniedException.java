package com.zepic.attendance_platform.exception;

public class CourseAccessDeniedException extends ApiException {

    public CourseAccessDeniedException() {
        super("COURSE_ACCESS_DENIED", "You are not allowed to manage this course");
    }
}
