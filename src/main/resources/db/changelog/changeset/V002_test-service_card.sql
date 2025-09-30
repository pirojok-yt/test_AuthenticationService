--liquibase formatted sql

--changeset pirojok:V002_test-service_card
--preconditions onFail:MARK_RAN
--precondition-not
--precondition-table-exists tableName=card
--endPreconditions

CREATE TABLE card (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  owner_id BIGINT NOT NULL,
  number text NOT NULL,
  last4_numbers varchar(4),
  expires_at TIMESTAMPTZ DEFAULT (now() + interval '4 years'),
  balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
  status varchar(16) NOT NULL,
  version INTEGER NOT NULL DEFAULT 1,

  CONSTRAINT fk_card_owner FOREIGN KEY (owner_id) REFERENCES users(id),
  CONSTRAINT positive_balance CHECK (balance >= 0)
);