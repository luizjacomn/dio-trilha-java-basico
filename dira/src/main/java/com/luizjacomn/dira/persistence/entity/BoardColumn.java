package com.luizjacomn.dira.persistence.entity;

import com.luizjacomn.dira.persistence.enumeration.Kind;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@DiraTable("board_column")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class BoardColumn implements DiraEntity {

    private Long id;

    @EqualsAndHashCode.Include
    private String name;

    private Integer order;

    private Kind kind;

    @EqualsAndHashCode.Include
    private Board board;

    public BoardColumn(Long id) {
        this.id = id;
    }

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Card> cards = new ArrayList<>();

}
