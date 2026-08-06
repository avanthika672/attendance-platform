-- V4__create_courses.sql
-- Creates "course" (owned by one teacher, scoped to college) and
-- "course_enrollment" (student <-> course mapping, scoped to college).

CREATE TABLE IF NOT EXISTS course (
    id          BIGSERIAL PRIMARY KEY,
    college_id  BIGINT NOT NULL REFERENCES college (id) ON DELETE RESTRICT,
    teacher_id  BIGINT NOT NULL REFERENCES app_user (id) ON DELETE RESTRICT,
    code        VARCHAR(50) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    semester    VARCHAR(20) NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_course_college_id_code
    ON course (college_id, code);

CREATE INDEX IF NOT EXISTS idx_course_college_id
    ON course (college_id);

CREATE INDEX IF NOT EXISTS idx_course_teacher_id
    ON course (teacher_id);

CREATE TABLE IF NOT EXISTS course_enrollment (
    id          BIGSERIAL PRIMARY KEY,
    college_id  BIGINT NOT NULL REFERENCES college (id) ON DELETE RESTRICT,
    course_id   BIGINT NOT NULL REFERENCES course (id) ON DELETE CASCADE,
    student_id  BIGINT NOT NULL REFERENCES student (id) ON DELETE CASCADE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- A student cannot be enrolled twice in the same course.
CREATE UNIQUE INDEX IF NOT EXISTS ux_course_enrollment_course_id_student_id
    ON course_enrollment (course_id, student_id);

CREATE INDEX IF NOT EXISTS idx_course_enrollment_college_id
    ON course_enrollment (college_id);

CREATE INDEX IF NOT EXISTS idx_course_enrollment_student_id
    ON course_enrollment (student_id);