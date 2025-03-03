package com.luizjacomn.designpatterns.model.entity;

import com.luizjacomn.designpatterns.model.entity.level.ClientLevelState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Entity
public class Client implements Serializable {

    @Serial
    private static final long serialVersionUID = 2721852588234322089L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Address address;

    @Enumerated
    @Column(nullable = false)
    private ClientLevelState.Level level = ClientLevelState.Level.ASSOCIATE;

    public void makeAssociate() {
        level.getState().associate(this);
    }

    public void makePro() {
        level.getState().pro(this);
    }

    public void makePlus() {
        level.getState().plus(this);
    }

    public void makePrime() {
        level.getState().prime(this);
    }

}
