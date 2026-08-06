-- V3__create_students.sql
-- Creates "department" (required as a foreign key target for student.department_id)

CREATE TABLE IF NOT EXISTS department (
    id          BIGSERIAL PRIMARY KEY,
    college_id  BIGINT NOT NULL REFERENCES college (id) ON DELETE RESTRICT,
    name        VARCHAR(255) NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_department_college_id_name
    ON department (college_id, name);

CREATE TABLE IF NOT EXISTS student (
    id             BIGSERIAL PRIMARY KEY,
    college_id     BIGINT NOT NULL REFERENCES college (id) ON DELETE RESTRICT,
    user_id        BIGINT REFERENCES app_user (id) ON DELETE RESTRICT,
    department_id  BIGINT NOT NULL REFERENCES department (id) ON DELETE RESTRICT,
    roll_number    VARCHAR(50) NOT NULL,
    first_name     VARCHAR(100) NOT NULL,
    last_name      VARCHAR(100) NOT NULL,
    status         VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Roll number must be unique within a college.
CREATE UNIQUE INDEX IF NOT EXISTS ux_student_college_id_roll_number
    ON student (college_id, roll_number);

-- Each app_user (STUDENT role) maps to at most one student record.
CREATE UNIQUE INDEX IF NOT EXISTS ux_student_user_id
    ON student (user_id);

CREATE INDEX IF NOT EXISTS idx_student_college_id
    ON student (college_id);

CREATE INDEX IF NOT EXISTS idx_student_department_id
    ON student (department_id);