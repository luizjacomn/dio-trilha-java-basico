package com.luizjacomn.dira.persistence.enumeration;

import java.util.stream.Stream;

public enum Kind {

    INITIAL, FINAL, CANCEL, PENDING;

    public static Kind findByName(final String name){
        return Stream.of(Kind.values())
                .filter(b -> b.name().equals(name))
                .findFirst().orElseThrow();
    }

}
