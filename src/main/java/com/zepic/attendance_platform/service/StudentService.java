package com.zepic.attendance_platform.service;

import com.zepic.attendance_platform.dto.request.CreateStudentRequest;
import com.zepic.attendance_platform.dto.response.PagedResponse;
import com.zepic.attendance_platform.dto.response.StudentResponse;
import com.zepic.attendance_platform.entity.AppUser;
import com.zepic.attendance_platform.entity.College;
import com.zepic.attendance_platform.entity.Department;
import com.zepic.attendance_platform.entity.Student;
import com.zepic.attendance_platform.exception.AuthenticationRequiredException;
import com.zepic.attendance_platform.exception.DepartmentNotFoundException;
import com.zepic.attendance_platform.exception.StudentEmailAlreadyExistsException;
import com.zepic.attendance_platform.exception.StudentNotFoundException;
import com.zepic.attendance_platform.exception.StudentRollNumberAlreadyExistsException;
import com.zepic.attendance_platform.exception.TeacherRoleRequiredException;
import com.zepic.attendance_platform.mapper.StudentMapper;
import com.zepic.attendance_platform.repository.AppUserRepository;
import com.zepic.attendance_platform.repository.CollegeRepository;
import com.zepic.attendance_platform.repository.DepartmentRepository;
import com.zepic.attendance_platform.repository.StudentRepository;
import com.zepic.attendance_platform.security.AuthenticatedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CollegeRepository collegeRepository;
    private final AppUserRepository appUserRepository;
    private final StudentMapper studentMapper;

    public StudentService(
            StudentRepository studentRepository,
            DepartmentRepository departmentRepository,
            CollegeRepository collegeRepository,
            AppUserRepository appUserRepository,
            StudentMapper studentMapper) {

        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.collegeRepository = collegeRepository;
        this.appUserRepository = appUserRepository;
        this.studentMapper = studentMapper;
    }

    @Transactional(readOnly = true)
    public PagedResponse<StudentResponse> listStudents(
            AuthenticatedUser authUser,
            String search,
            Pageable pageable) throws AuthenticationRequiredException,
            TeacherRoleRequiredException {

        requireTeacher(authUser);

        Page<Student> studentsPage;

        if (search != null && !search.isBlank()) {
            studentsPage = studentRepository.searchByCollegeId(
                    authUser.collegeId(),
                    search,
                    pageable
            );
        } else {
            studentsPage = studentRepository.findByCollege_Id(
                    authUser.collegeId(),
                    pageable
            );
        }

        return toPagedResponse(
                studentsPage.map(studentMapper::toResponse)
        );
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(
            AuthenticatedUser authUser,
            Long studentId) throws AuthenticationRequiredException,
            TeacherRoleRequiredException,
            StudentNotFoundException {

        requireTeacher(authUser);

        Student student = studentRepository
                .findByIdAndCollege_Id(
                        studentId,
                        authUser.collegeId()
                )
                .orElseThrow(StudentNotFoundException::new);

        return studentMapper.toResponse(student);
    }

    @Transactional
    public StudentResponse createStudent(
            AuthenticatedUser authUser,
            CreateStudentRequest request) throws AuthenticationRequiredException,
            TeacherRoleRequiredException,
            DepartmentNotFoundException,
            StudentRollNumberAlreadyExistsException,
            StudentEmailAlreadyExistsException {

        requireTeacher(authUser);

        Long collegeId = authUser.collegeId();

        Department department = departmentRepository
                .findByIdAndCollege_Id(
                        request.getDepartmentId(),
                        collegeId
                )
                .orElseThrow(DepartmentNotFoundException::new);

        if (studentRepository.existsByCollege_IdAndRollNumber(
                collegeId,
                request.getRollNumber())) {

            throw new StudentRollNumberAlreadyExistsException();
        }

        if (appUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new StudentEmailAlreadyExistsException();
        }

        College college =
                collegeRepository.getReferenceById(collegeId);

        Student student = studentMapper.toEntity(request);

        student.setCollege(college);
        student.setDepartment(department);
        student.setStatus("ACTIVE");

        Instant now = Instant.now();

        student.setCreatedAt(now);
        student.setUpdatedAt(now);

        student = studentRepository.save(student);

        return studentMapper.toResponse(student);
    }

    private AppUser requireTeacher(
            AuthenticatedUser authUser) throws AuthenticationRequiredException,
            TeacherRoleRequiredException {

        if (authUser == null) {
            throw new AuthenticationRequiredException();
        }

        AppUser user = appUserRepository
                .findByIdAndCollege_Id(
                        authUser.userId(),
                        authUser.collegeId()
                )
                .orElseThrow(
                        AuthenticationRequiredException::new
                );

        if (!"TEACHER".equals(user.getRole())) {
            throw new TeacherRoleRequiredException();
        }

        return user;
    }

    private <T> PagedResponse<T> toPagedResponse(
            Page<T> page) {

        return PagedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}