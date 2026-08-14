package com.zepic.attendance_platform.controller;

import com.zepic.attendance_platform.dto.request.CreateStudentRequest;
import com.zepic.attendance_platform.dto.response.PagedResponse;
import com.zepic.attendance_platform.dto.response.StudentResponse;
import com.zepic.attendance_platform.exception.AuthenticationRequiredException;
import com.zepic.attendance_platform.exception.DepartmentNotFoundException;
import com.zepic.attendance_platform.exception.StudentEmailAlreadyExistsException;
import com.zepic.attendance_platform.exception.StudentNotFoundException;
import com.zepic.attendance_platform.exception.StudentRollNumberAlreadyExistsException;
import com.zepic.attendance_platform.exception.TeacherRoleRequiredException;
import com.zepic.attendance_platform.security.AuthenticatedUser;
import com.zepic.attendance_platform.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private static final String AUTHENTICATED_USER_ATTRIBUTE = "authenticatedUser";

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<StudentResponse>> listStudents(
            HttpServletRequest request,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20) Pageable pageable)
            throws AuthenticationRequiredException, TeacherRoleRequiredException {

        AuthenticatedUser authUser = resolveAuthenticatedUser(request);
        PagedResponse<StudentResponse> response = studentService.listStudents(authUser, search, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getStudentById(
            HttpServletRequest request,
            @PathVariable Long studentId)
            throws AuthenticationRequiredException, TeacherRoleRequiredException, StudentNotFoundException {

        AuthenticatedUser authUser = resolveAuthenticatedUser(request);
        StudentResponse response = studentService.getStudentById(authUser, studentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(
            HttpServletRequest request,
            @RequestBody CreateStudentRequest createStudentRequest)
            throws AuthenticationRequiredException, TeacherRoleRequiredException, DepartmentNotFoundException,
            StudentRollNumberAlreadyExistsException, StudentEmailAlreadyExistsException {

        AuthenticatedUser authUser = resolveAuthenticatedUser(request);
        StudentResponse response = studentService.createStudent(authUser, createStudentRequest);
        return ResponseEntity.created(URI.create("/api/students/" + response.getId())).body(response);
    }

    private AuthenticatedUser resolveAuthenticatedUser(HttpServletRequest request) {
        Object attribute = request.getAttribute(AUTHENTICATED_USER_ATTRIBUTE);
        if (attribute instanceof AuthenticatedUser authenticatedUser) {
            return authenticatedUser;
        }
        throw new IllegalStateException(
                "No AuthenticatedUser was found on the request attribute \"" + AUTHENTICATED_USER_ATTRIBUTE + "\". "
                        + "Session/cookie resolution has not been implemented yet in this project.");
    }
}