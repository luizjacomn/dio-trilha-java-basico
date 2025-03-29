-- liquibase formatted sql
-- changeset luizjacomn:202503251038
-- comment: board table create

CREATE TABLE IF NOT EXISTS board (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- rollback DROP TABLE board
