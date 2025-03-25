-- liquibase formatted sql
-- changeset luizjacomn:202503251134
-- comment: card table create

CREATE TABLE IF NOT EXISTS card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    board_column_id BIGINT NOT NULL,

    CONSTRAINT card__board_column_fk FOREIGN KEY (board_column_id) REFERENCES board_column (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- rollback DROP TABLE card
