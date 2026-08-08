package com.zepic.attendance_platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {
        "loginSessions",
        "student",
        "courses",
        "attendanceRecords"
})
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", nullable = false)
    private College college;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<LoginSession> loginSessions;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Student student;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Course> courses;

    @OneToMany(mappedBy = "markedBy", fetch = FetchType.LAZY)
    private List<AttendanceRecord> attendanceRecords;
}