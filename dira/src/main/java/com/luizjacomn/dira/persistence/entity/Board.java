package com.luizjacomn.dira.persistence.entity;

import com.luizjacomn.dira.persistence.enumeration.Kind;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.function.Predicate;

@DiraTable("board")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Board implements DiraEntity {

    private Long id;

    @EqualsAndHashCode.Include
    private String name;

    public Board(Long id) {
        this.id = id;
    }

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BoardColumn> boardColumns;

    public BoardColumn getInitialColumn() {
        return this.getFilteredColumn(bc -> Kind.INITIAL.equals(bc.getKind()));
    }

    public BoardColumn getCancelColumn() {
        return this.getFilteredColumn(bc -> Kind.CANCEL.equals(bc.getKind()));
    }

    private BoardColumn getFilteredColumn(Predicate<BoardColumn> filter) {
        return boardColumns.stream()
                .filter(filter)
                .findFirst().orElseThrow();
    }

}
