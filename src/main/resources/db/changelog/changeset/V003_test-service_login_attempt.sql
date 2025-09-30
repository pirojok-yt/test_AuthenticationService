--liquibase formatted sql

--changeset pirojok:V003_test-service_login_attempt
--preconditions onFail:MARK_RAN
--precondition-not
--precondition-table-exists tableName=login_attempt
--endPreconditions

CREATE TABLE login_attempt (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(32) NOT NULL,
    success BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);