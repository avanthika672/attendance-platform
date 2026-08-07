package com.zepic.attendance_platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {
        "courseEnrollments",
        "attendanceRecords"
})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", nullable = false)
    private com.zepic.attendance_platform.entity.College college;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private com.zepic.attendance_platform.entity.AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private com.zepic.attendance_platform.entity.Department department;

    @Column(name = "roll_number", nullable = false)
    private String rollNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<com.zepic.attendance_platform.entity.CourseEnrollment> courseEnrollments;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<com.zepic.attendance_platform.entity.AttendanceRecord> attendanceRecords;
}
