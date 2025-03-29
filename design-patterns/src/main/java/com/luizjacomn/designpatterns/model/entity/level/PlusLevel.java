package com.luizjacomn.designpatterns.model.entity.level;

import com.luizjacomn.designpatterns.exception.InvalidLevelTransitionException;
import com.luizjacomn.designpatterns.model.entity.Client;

public class PlusLevel implements ClientLevelState {

    @Override
    public void associate(Client client) {
        throw new InvalidLevelTransitionException("Cliente não pode passar para o nível ASSOCIATE quando é PLUS");
    }

    @Override
    public void pro(Client client) {
        client.setLevel(Level.PRO);
    }

    @Override
    public void plus(Client client) {
        throw new InvalidLevelTransitionException("Cliente já é PLUS");
    }

    @Override
    public void prime(Client client) {
        client.setLevel(Level.PRIME);
    }

}
