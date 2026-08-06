CREATE TABLE IF NOT EXISTS college (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(255) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE college
    ADD CONSTRAINT uq_college_name UNIQUE(name);

COMMENT ON TABLE college IS
'Represents one tenant (college).';