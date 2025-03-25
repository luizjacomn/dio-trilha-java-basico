-- liquibase formatted sql
-- changeset luizjacomn:202503251121
-- comment: board_column table create

CREATE TABLE IF NOT EXISTS board_column (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    `order` INT NOT NULL,
    kind VARCHAR(10) NOT NULL,
    board_id BIGINT NOT NULL,

    CONSTRAINT board_column__board_fk FOREIGN KEY (board_id) REFERENCES board (id) ON DELETE CASCADE,
    CONSTRAINT id_order_uk UNIQUE KEY (board_id, `order`)
) ENGINE=InnoDB;

-- rollback DROP TABLE board_column
