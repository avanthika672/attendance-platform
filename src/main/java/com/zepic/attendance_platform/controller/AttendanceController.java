package com.zepic.attendance_platform.controller;

import com.zepic.attendance_platform.dto.request.MarkAttendanceRequest;
import com.zepic.attendance_platform.dto.response.AttendanceResponse;
import com.zepic.attendance_platform.dto.response.AttendanceSummaryResponse;
import com.zepic.attendance_platform.dto.response.PagedResponse;
import com.zepic.attendance_platform.exception.AttendanceAlreadyExistsException;
import com.zepic.attendance_platform.exception.AuthenticationRequiredException;
import com.zepic.attendance_platform.exception.CourseAccessDeniedException;
import com.zepic.attendance_platform.exception.CourseNotFoundException;
import com.zepic.attendance_platform.exception.InvalidAttendanceStatusException;
import com.zepic.attendance_platform.exception.StudentNotFoundException;
import com.zepic.attendance_platform.exception.TeacherRoleRequiredException;
import com.zepic.attendance_platform.security.AuthenticatedUser;
import com.zepic.attendance_platform.service.AttendanceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private static final String AUTHENTICATED_USER_ATTRIBUTE = "authenticatedUser";

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<AttendanceResponse> markAttendance(
            HttpServletRequest request,
            @RequestBody MarkAttendanceRequest markAttendanceRequest)
            throws AuthenticationRequiredException, TeacherRoleRequiredException, InvalidAttendanceStatusException,
            CourseNotFoundException, CourseAccessDeniedException, StudentNotFoundException,
            AttendanceAlreadyExistsException {

        AuthenticatedUser authUser = resolveAuthenticatedUser(request);
        AttendanceResponse response = attendanceService.markAttendance(authUser, markAttendanceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

   @GetMapping
    public ResponseEntity<PagedResponse<AttendanceResponse>> listAttendanceForTeacher(
            HttpServletRequest request,
            @RequestParam Long courseId,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20) Pageable pageable)
            throws AuthenticationRequiredException, TeacherRoleRequiredException, CourseNotFoundException,
            CourseAccessDeniedException {

        AuthenticatedUser authUser = resolveAuthenticatedUser(request);
        PagedResponse<AttendanceResponse> response = attendanceService.listAttendanceForTeacher(
                authUser, courseId, date, studentId, status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<AttendanceSummaryResponse> getAttendanceSummary(
            HttpServletRequest request,
            @RequestParam Long courseId,
            @RequestParam LocalDate date)
            throws AuthenticationRequiredException, TeacherRoleRequiredException, CourseNotFoundException,
            CourseAccessDeniedException {

        AuthenticatedUser authUser = resolveAuthenticatedUser(request);
        AttendanceSummaryResponse response = attendanceService.getAttendanceSummary(authUser, courseId, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<PagedResponse<AttendanceResponse>> getStudentAttendanceHistory(
            HttpServletRequest request,
            @PathVariable Long studentId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @PageableDefault(size = 20) Pageable pageable)
            throws AuthenticationRequiredException, TeacherRoleRequiredException, StudentNotFoundException {

        AuthenticatedUser authUser = resolveAuthenticatedUser(request);
        PagedResponse<AttendanceResponse> response = attendanceService.getStudentAttendanceHistory(
                authUser, studentId, courseId, fromDate, toDate, pageable);
        return ResponseEntity.ok(response);
    }

    private AuthenticatedUser resolveAuthenticatedUser(HttpServletRequest request) {
        Object attribute = request.getAttribute(AUTHENTICATED_USER_ATTRIBUTE);

        if (attribute instanceof AuthenticatedUser authenticatedUser) {
            return authenticatedUser;
        }

        throw new IllegalStateException(
                "No AuthenticatedUser was found on the request attribute \""
                        + AUTHENTICATED_USER_ATTRIBUTE
                        + "\". Session/cookie resolution has not been implemented yet in this project.");
    }
}