package com.luizjacomn.model;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Space>> spaces;

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatus getStatus() {
        if (spaces.stream().flatMap(Collection::stream).noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))) {
            return GameStatus.NON_STARTED;
        }

        return spaces.stream().flatMap(Collection::stream)
                     .anyMatch(s -> isNull(s.getActual())) ? GameStatus.INCOMPLETE : GameStatus.COMPLETE;
    }

    public boolean hasErrors() {
        if (GameStatus.NON_STARTED.equals(this.getStatus())) {
            return false;
        }

        return spaces.stream().flatMap(Collection::stream)
                     .anyMatch(s -> nonNull(s.getActual()) && !s.getActual().equals(s.getExpected()));
    }

    public boolean changeValue(final int col, final int row, final int value) {
        var space = spaces.get(col).get(row);

        if (space.isFixed()) {
            return false;
        }

        space.setActual(value);

        return true;
    }

    public boolean clearValue(final int col, final int row) {
        var space = spaces.get(col).get(row);

        if (space.isFixed()) {
            return false;
        }

        space.clearSpace();

        return true;
    }

    public void reset() {
        spaces.forEach(c -> c.forEach(Space::clearSpace));
    }

    public boolean gameIsFinished() {
        return !hasErrors() && getStatus().equals(GameStatus.COMPLETE);
    }

}
