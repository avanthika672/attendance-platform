-- V5__create_attendance.sql
-- Creates "attendance_record", scoped to college, supporting marking,
-- listing, history, and summary queries defined in the API contract.

CREATE TABLE IF NOT EXISTS attendance_record (
    id                BIGSERIAL PRIMARY KEY,
    college_id        BIGINT NOT NULL REFERENCES college (id) ON DELETE RESTRICT,
    course_id         BIGINT NOT NULL REFERENCES course (id) ON DELETE RESTRICT,
    student_id        BIGINT NOT NULL REFERENCES student (id) ON DELETE RESTRICT,
    marked_by         BIGINT NOT NULL REFERENCES app_user (id) ON DELETE RESTRICT,
    attendance_date   DATE NOT NULL,
    status            VARCHAR(20) NOT NULL,
    remarks           TEXT,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_attendance_record_status CHECK (status IN ('PRESENT', 'ABSENT', 'LATE'))
);

-- Prevents a duplicate attendance record for the same student/course/date.
CREATE UNIQUE INDEX IF NOT EXISTS ux_attendance_record_student_course_date
    ON attendance_record (student_id, course_id, attendance_date);

CREATE INDEX IF NOT EXISTS idx_attendance_record_college_id
    ON attendance_record (college_id);

-- Supports GET /api/attendance?courseId=&date= and the summary endpoint.
CREATE INDEX IF NOT EXISTS idx_attendance_record_course_id_date
    ON attendance_record (course_id, attendance_date);

-- Supports GET /api/attendance/me?courseId=&from=&to=
CREATE INDEX IF NOT EXISTS idx_attendance_record_student_id_date
    ON attendance_record (student_id, attendance_date);

CREATE INDEX IF NOT EXISTS idx_attendance_record_marked_by
    ON attendance_record (marked_by);