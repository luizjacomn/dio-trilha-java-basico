-- liquibase formatted sql
-- changeset luizjacomn:202503251139
-- comment: block table create

CREATE TABLE IF NOT EXISTS block (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    block_reason VARCHAR(255) NOT NULL,
    blocked_at TIMESTAMP NOT NULL,
    unblocked_at TIMESTAMP NULL,
    unblock_reason VARCHAR(255) NULL,
    card_id BIGINT NOT NULL,

    CONSTRAINT block__card_fk FOREIGN KEY (card_id) REFERENCES card (id) ON DELETE CASCADE,
    CONSTRAINT chk_unblock_data CHECK (
        (unblocked_at IS NOT NULL AND unblock_reason IS NOT NULL) OR
        (unblocked_at IS NULL AND unblock_reason IS NULL)
    )
) ENGINE=InnoDB;

-- rollback DROP TABLE block
