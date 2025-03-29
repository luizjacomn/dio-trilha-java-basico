package com.luizjacomn.designpatterns.model.entity.level;

import com.luizjacomn.designpatterns.exception.InvalidLevelTransitionException;
import com.luizjacomn.designpatterns.model.entity.Client;

public class PrimeLevel implements ClientLevelState {

    @Override
    public void associate(Client client) {
        throw new InvalidLevelTransitionException("Cliente não pode passar para o nível ASSOCIATE quando é PRIME");
    }

    @Override
    public void pro(Client client) {
        throw new InvalidLevelTransitionException("Cliente não pode passar para o nível PRO quando é PRIME");
    }

    @Override
    public void plus(Client client) {
        client.setLevel(Level.PLUS);
    }

    @Override
    public void prime(Client client) {
        throw new InvalidLevelTransitionException("Cliente já é PRIME");
    }

}
