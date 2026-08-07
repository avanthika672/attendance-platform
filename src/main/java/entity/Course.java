package com.zepic.attendance_platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {
        "courseEnrollments",
        "attendanceRecords"
})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", nullable = false)
    private com.zepic.attendance_platform.entity.College college;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private com.zepic.attendance_platform.entity.AppUser teacher;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "semester", nullable = false)
    private String semester;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<com.zepic.attendance_platform.entity.CourseEnrollment> courseEnrollments;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<com.zepic.attendance_platform.entity.AttendanceRecord> attendanceRecords;
}