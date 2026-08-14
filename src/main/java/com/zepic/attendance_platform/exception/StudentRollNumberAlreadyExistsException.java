package com.zepic.attendance_platform.exception;
public class StudentRollNumberAlreadyExistsException extends ApiException {
    public StudentRollNumberAlreadyExistsException () {
        super("STUDENT_ROLL_NUMBER_ALREADY_EXISTS", "A student with this roll number already exists");

    }

}