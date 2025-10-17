-- Flyway Migration Script V1__Initial_Schema.sql
-- PostgreSQL 17 compatible
-- Description: Initial database schema for SDL Backend application

-- Create custom ENUM types
CREATE TYPE status AS ENUM ('COMPLETED', 'NOT_COMPLETED');

-- Create users table
CREATE TABLE users
(
    id         UUID PRIMARY KEY                  DEFAULT gen_random_uuid(),
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    email      VARCHAR(255)             NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted    BOOLEAN                  NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP WITH TIME ZONE
);

-- Create document table
CREATE TABLE document
(
    id                  UUID PRIMARY KEY                  DEFAULT gen_random_uuid(),
    file_name           VARCHAR(500)             NOT NULL,
    secondary_file_name VARCHAR(500)             NOT NULL UNIQUE,
    subsections_count   INTEGER                  NOT NULL DEFAULT 0,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted             BOOLEAN                  NOT NULL DEFAULT FALSE,
    deleted_at          TIMESTAMP WITH TIME ZONE
);

-- Create subsection table
CREATE TABLE subsection
(
    id          UUID PRIMARY KEY                  DEFAULT gen_random_uuid(),
    document_id UUID                     NOT NULL,
    parent_id   UUID,
    title       VARCHAR(500)             NOT NULL,
    content     TEXT                     NOT NULL,
    position    INTEGER                  NOT NULL,
    level       INTEGER                  NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_subsection_document FOREIGN KEY (document_id)
        REFERENCES document (id) ON DELETE CASCADE,
    CONSTRAINT fk_subsection_parent FOREIGN KEY (parent_id)
        REFERENCES subsection (id) ON DELETE CASCADE
);

-- Create session table
CREATE TABLE session
(
    id                 UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    name               VARCHAR(255) NOT NULL,
    description        TEXT,
    status             status       NOT NULL DEFAULT 'NOT_COMPLETED',
    review             TEXT,
    start_time         TIMESTAMP WITH TIME ZONE,
    end_time           TIMESTAMP WITH TIME ZONE,
    document_id        UUID         NOT NULL,
    created_by_user_id UUID         NOT NULL,
    CONSTRAINT fk_session_document FOREIGN KEY (document_id)
        REFERENCES document (id) ON DELETE RESTRICT,
    CONSTRAINT fk_session_user FOREIGN KEY (created_by_user_id)
        REFERENCES users (id) ON DELETE RESTRICT
);

-- Create cycle table
CREATE TABLE cycle
(
    id          UUID PRIMARY KEY                  DEFAULT gen_random_uuid(),
    session_id  UUID                     NOT NULL,
    review      TEXT,
    cycle_order INTEGER                  NOT NULL,
    status      status                   NOT NULL DEFAULT 'NOT_COMPLETED',
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_cycle_session FOREIGN KEY (session_id)
        REFERENCES session (id) ON DELETE CASCADE
);

-- Create question table
CREATE TABLE question
(
    id                                    UUID PRIMARY KEY                  DEFAULT gen_random_uuid(),
    session_id                            UUID                     NOT NULL,
    question_text                         TEXT                     NOT NULL,
    option_a                              TEXT                     NOT NULL,
    option_b                              TEXT                     NOT NULL,
    option_c                              TEXT                     NOT NULL,
    option_d                              TEXT                     NOT NULL,
    correct_option                        CHAR(1)                  NOT NULL,
    explanation                           TEXT,
    explanation_to_generate_this_question TEXT,
    question_order                        INTEGER                  NOT NULL DEFAULT 1,
    submitted_answer                      CHAR(1),
    answered_at                           TIMESTAMP WITH TIME ZONE,
    created_at                            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_question_session FOREIGN KEY (session_id)
        REFERENCES session (id) ON DELETE CASCADE
);

-- Add comments to tables for documentation
COMMENT ON TABLE users IS 'Stores user account information with soft delete support';
COMMENT ON TABLE document IS 'Stores uploaded PDF documents metadata';
COMMENT ON TABLE subsection IS 'Stores hierarchical document sections and subsections';
COMMENT ON TABLE session IS 'Stores interview session information';
COMMENT ON TABLE cycle IS 'Stores interview cycles within a session';
COMMENT ON TABLE question IS 'Stores interview questions and answers';

COMMENT ON COLUMN users.deleted IS 'Soft delete flag';
COMMENT ON COLUMN users.deleted_at IS 'Timestamp when record was soft deleted';
COMMENT ON COLUMN document.secondary_file_name IS 'User-friendly document name';
COMMENT ON COLUMN subsection.level IS 'Hierarchy level (0 = root section)';
COMMENT ON COLUMN subsection.position IS 'Position within the same level';
COMMENT ON COLUMN question.correct_option IS 'The correct answer (A, B, C, or D)';
COMMENT ON COLUMN question.submitted_answer IS 'User submitted answer (A, B, C, or D)';
COMMENT ON COLUMN question.explanation_to_generate_this_question IS 'AI explanation for why this question was generated';