package com.luizjacomn.dira.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@DiraTable("block")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Block implements DiraEntity {

    @EqualsAndHashCode.Include
    private Long id;

    private OffsetDateTime blockedAt;

    private String blockReason;

    private OffsetDateTime unblockedAt;

    private String unblockReason;

    private Card card;

}
