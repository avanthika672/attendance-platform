package com.zepic.attendance_platform.service;

import com.zepic.attendance_platform.dto.request.MarkAttendanceRequest;
import com.zepic.attendance_platform.dto.response.AttendanceResponse;
import com.zepic.attendance_platform.dto.response.AttendanceSummaryResponse;
import com.zepic.attendance_platform.dto.response.PagedResponse;
import com.zepic.attendance_platform.entity.AppUser;
import com.zepic.attendance_platform.entity.AttendanceRecord;
import com.zepic.attendance_platform.entity.Course;
import com.zepic.attendance_platform.entity.Student;
import com.zepic.attendance_platform.exception.AttendanceAlreadyExistsException;
import com.zepic.attendance_platform.exception.AuthenticationRequiredException;
import com.zepic.attendance_platform.exception.CourseAccessDeniedException;
import com.zepic.attendance_platform.exception.CourseNotFoundException;
import com.zepic.attendance_platform.exception.InvalidAttendanceStatusException;
import com.zepic.attendance_platform.exception.StudentNotFoundException;
import com.zepic.attendance_platform.exception.TeacherRoleRequiredException;
import com.zepic.attendance_platform.mapper.AttendanceMapper;
import com.zepic.attendance_platform.repository.AppUserRepository;
import com.zepic.attendance_platform.repository.AttendanceRecordRepository;
import com.zepic.attendance_platform.repository.CourseEnrollmentRepository;
import com.zepic.attendance_platform.repository.CourseRepository;
import com.zepic.attendance_platform.repository.StudentRepository;
import com.zepic.attendance_platform.security.AuthenticatedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Service
public class AttendanceService {

    private static final Set<String> VALID_STATUSES = Set.of("PRESENT", "ABSENT", "LATE");

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final AppUserRepository appUserRepository;
    private final AttendanceMapper attendanceMapper;

    public AttendanceService(AttendanceRecordRepository attendanceRecordRepository,
                             CourseRepository courseRepository,
                             StudentRepository studentRepository,
                             CourseEnrollmentRepository courseEnrollmentRepository,
                             AppUserRepository appUserRepository,
                             AttendanceMapper attendanceMapper) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.appUserRepository = appUserRepository;
        this.attendanceMapper = attendanceMapper;
    }

    @Transactional
    public AttendanceResponse markAttendance(AuthenticatedUser authUser, MarkAttendanceRequest request) throws InvalidAttendanceStatusException,
            AuthenticationRequiredException,
            TeacherRoleRequiredException,
            CourseNotFoundException,
            CourseAccessDeniedException,
            StudentNotFoundException,
            AttendanceAlreadyExistsException {
        if (!VALID_STATUSES.contains(request.getStatus())) {
            throw new InvalidAttendanceStatusException();
        }

        AppUser teacher = requireTeacher(authUser);

        Course course = courseRepository.findByIdAndCollege_Id(request.getCourseId(), authUser.collegeId())
                .orElseThrow(CourseNotFoundException::new);

        courseRepository.findByIdAndCollege_IdAndTeacher_Id(course.getId(), authUser.collegeId(), teacher.getId())
                .orElseThrow(CourseAccessDeniedException::new);

        Student student = studentRepository.findByIdAndCollege_Id(request.getStudentId(), authUser.collegeId())
                .orElseThrow(StudentNotFoundException::new);

        if (!courseEnrollmentRepository.existsByCourse_IdAndStudent_Id(course.getId(), student.getId())) {
            throw new StudentNotFoundException();
        }

        if (attendanceRecordRepository.existsByCourse_IdAndStudent_IdAndAttendanceDate(
                course.getId(), student.getId(), request.getAttendanceDate())) {
            throw new AttendanceAlreadyExistsException();
        }

        AttendanceRecord record = attendanceMapper.toEntity(request);
        record.setCollege(course.getCollege());
        record.setCourse(course);
        record.setStudent(student);
        record.setMarkedBy(teacher);

        Instant now = Instant.now();
        record.setCreatedAt(now);
        record.setUpdatedAt(now);

        record = attendanceRecordRepository.save(record);

        return attendanceMapper.toResponse(record);
    }

    @Transactional(readOnly = true)
    public PagedResponse<AttendanceResponse> listAttendanceForTeacher(AuthenticatedUser authUser,
                                                                      Long courseId,
                                                                      LocalDate date,
                                                                      Long studentId,
                                                                      String status,
                                                                      Pageable pageable) throws AuthenticationRequiredException,
            TeacherRoleRequiredException,
            CourseNotFoundException,
            CourseAccessDeniedException {
        AppUser teacher = requireTeacher(authUser);

        Course course = courseRepository.findByIdAndCollege_Id(courseId, authUser.collegeId())
                .orElseThrow(CourseNotFoundException::new);

        courseRepository.findByIdAndCollege_IdAndTeacher_Id(course.getId(), authUser.collegeId(), teacher.getId())
                .orElseThrow(CourseAccessDeniedException::new);

        Page<AttendanceRecord> page = attendanceRecordRepository.findForTeacherListing(
                authUser.collegeId(), course.getId(), date, studentId, status, pageable);

        return toPagedResponse(page.map(attendanceMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public AttendanceSummaryResponse getAttendanceSummary(AuthenticatedUser authUser, Long courseId, LocalDate date) throws AuthenticationRequiredException,
            TeacherRoleRequiredException,
            CourseNotFoundException,
            CourseAccessDeniedException {
        AppUser teacher = requireTeacher(authUser);

        Course course = courseRepository.findByIdAndCollege_Id(courseId, authUser.collegeId())
                .orElseThrow(CourseNotFoundException::new);

        courseRepository.findByIdAndCollege_IdAndTeacher_Id(course.getId(), authUser.collegeId(), teacher.getId())
                .orElseThrow(CourseAccessDeniedException::new);

        long totalStudents = courseEnrollmentRepository.countByCourse_Id(course.getId());
        long marked = attendanceRecordRepository.countByCourse_IdAndAttendanceDate(course.getId(), date);
        long present = attendanceRecordRepository.countByCourse_IdAndAttendanceDateAndStatus(course.getId(), date, "PRESENT");
        long absent = attendanceRecordRepository.countByCourse_IdAndAttendanceDateAndStatus(course.getId(), date, "ABSENT");
        long late = attendanceRecordRepository.countByCourse_IdAndAttendanceDateAndStatus(course.getId(), date, "LATE");
        long unmarked = Math.max(0, totalStudents - marked);

        double attendancePercentage = totalStudents == 0
                ? 0.0
                : Math.round(((present + late) * 1000.0 / totalStudents)) / 10.0;

        return AttendanceSummaryResponse.builder()
                .course(attendanceMapper.toCourseSummary(course))
                .date(date)
                .totalStudents(totalStudents)
                .present(present)
                .absent(absent)
                .late(late)
                .unmarked(unmarked)
                .attendancePercentage(attendancePercentage)
                .build();
    }

    @Transactional(readOnly = true)
    public PagedResponse<AttendanceResponse> getStudentAttendanceHistory(AuthenticatedUser authUser,
                                                                         Long studentId,
                                                                         Long courseId,
                                                                         LocalDate fromDate,
                                                                         LocalDate toDate,
                                                                         Pageable pageable) throws AuthenticationRequiredException,
            TeacherRoleRequiredException,
            StudentNotFoundException {
        requireTeacher(authUser);

        Student student = studentRepository.findByIdAndCollege_Id(studentId, authUser.collegeId())
                .orElseThrow(StudentNotFoundException::new);

        Page<AttendanceRecord> page = attendanceRecordRepository.findForStudentHistory(
                authUser.collegeId(), student.getId(), courseId, fromDate, toDate, pageable);

        return toPagedResponse(page.map(attendanceMapper::toResponse));
    }

    private AppUser requireTeacher(AuthenticatedUser authUser) throws AuthenticationRequiredException,
            TeacherRoleRequiredException {
        if (authUser == null) {
            throw new AuthenticationRequiredException();
        }
        AppUser user = appUserRepository.findByIdAndCollege_Id(authUser.userId(), authUser.collegeId())
                .orElseThrow(AuthenticationRequiredException::new);

        if (!"TEACHER".equals(user.getRole())) {
            throw new TeacherRoleRequiredException();
        }
        return user;
    }

    private <T> PagedResponse<T> toPagedResponse(Page<T> page) {
        return PagedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}