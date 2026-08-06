-- V2__create_users.sql
-- Creates the "app_user" and "login_session" tables.
-- app_user: every authenticated identity (TEACHER or STUDENT), scoped to one college.
-- login_session: opaque, cookie-based session tracking for authenticated users.

CREATE TABLE IF NOT EXISTS app_user (
    id             BIGSERIAL PRIMARY KEY,
    college_id     BIGINT NOT NULL REFERENCES college (id) ON DELETE RESTRICT,
    email          VARCHAR(255) NOT NULL,
    password_hash  VARCHAR(255) NOT NULL,
    display_name   VARCHAR(255) NOT NULL,
    role           VARCHAR(20) NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_app_user_role CHECK (role IN ('TEACHER', 'STUDENT'))
);

-- Email is looked up at login time before the college context is known,
-- so it must be unique across the whole platform, not just per college.
CREATE UNIQUE INDEX IF NOT EXISTS ux_app_user_email
    ON app_user (email);

CREATE INDEX IF NOT EXISTS idx_app_user_college_id
    ON app_user (college_id);

CREATE TABLE IF NOT EXISTS login_session (
    id          VARCHAR(128) PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES app_user (id) ON DELETE CASCADE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    expires_at  TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_login_session_user_id
    ON login_session (user_id);

CREATE INDEX IF NOT EXISTS idx_login_session_expires_at
    ON login_session (expires_at);