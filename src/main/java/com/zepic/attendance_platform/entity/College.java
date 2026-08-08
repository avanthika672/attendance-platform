package com.zepic.attendance_platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "college")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {
        "users",
        "departments",
        "students",
        "courses",
        "courseEnrollments",
        "attendanceRecords"
})
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "college", fetch = FetchType.LAZY)
    private List<AppUser> users;

    @OneToMany(mappedBy = "college", fetch = FetchType.LAZY)
    private List<Department> departments;

    @OneToMany(mappedBy = "college", fetch = FetchType.LAZY)
    private List<Student> students;

    @OneToMany(mappedBy = "college", fetch = FetchType.LAZY)
    private List<Course> courses;

    @OneToMany(mappedBy = "college", fetch = FetchType.LAZY)
    private List<CourseEnrollment> courseEnrollments;

    @OneToMany(mappedBy = "college", fetch = FetchType.LAZY)
    private List<AttendanceRecord> attendanceRecords;
}