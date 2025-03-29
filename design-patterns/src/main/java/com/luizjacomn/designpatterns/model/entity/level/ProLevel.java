package com.luizjacomn.designpatterns.model.entity.level;

import com.luizjacomn.designpatterns.exception.InvalidLevelTransitionException;
import com.luizjacomn.designpatterns.model.entity.Client;

public class ProLevel implements ClientLevelState {

    @Override
    public void associate(Client client) {
        client.setLevel(Level.ASSOCIATE);
    }

    @Override
    public void pro(Client client) {
        throw new InvalidLevelTransitionException("Cliente já é PRO");
    }

    @Override
    public void plus(Client client) {
        client.setLevel(Level.PLUS);
    }

    @Override
    public void prime(Client client) {
        throw new InvalidLevelTransitionException("Cliente não pode passar para o nível PRIME quando é PRO");
    }
}
