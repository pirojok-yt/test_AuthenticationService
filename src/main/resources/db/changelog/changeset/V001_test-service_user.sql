--liquibase formatted sql

--changeset pirojok:V001_test-service_user
--preconditions onFail:MARK_RAN
--precondition-not
--precondition-table-exists tableName=users
--endPreconditions

CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username varchar(32) NOT NULL,
    password varchar(256) NOT NULL,
    role varchar(16) NOT NULL
);