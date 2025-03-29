package com.luizjacomn.dira.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@DiraTable("card")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Card implements DiraEntity {

    private Long id;

    @EqualsAndHashCode.Include
    private String title;

    private String description;

    @EqualsAndHashCode.Include
    private BoardColumn boardColumn;

    public Card(Long id) {
        this.id = id;
    }

}
