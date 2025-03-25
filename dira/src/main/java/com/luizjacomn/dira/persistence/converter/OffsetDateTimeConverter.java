package com.luizjacomn.dira.persistence.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OffsetDateTimeConverter {

    public static OffsetDateTime toOffsetDateTime(final Timestamp value){
        return Objects.nonNull(value) ? OffsetDateTime.ofInstant(value.toInstant(), ZoneOffset.UTC) : null;
    }

    public static Timestamp toTimestamp(final OffsetDateTime value){
        return Objects.nonNull(value) ? Timestamp.valueOf(value.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()) : null;
    }

}
