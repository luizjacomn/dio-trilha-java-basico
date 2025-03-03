package com.luizjacomn.designpatterns.model.entity.level;

import com.luizjacomn.designpatterns.exception.InvalidLevelTransitionException;
import com.luizjacomn.designpatterns.model.entity.Client;

public class AssociateLevel implements ClientLevelState {

    @Override
    public void associate(Client client) {
        throw new InvalidLevelTransitionException("Cliente já é ASSOCIATE");
    }

    @Override
    public void pro(Client client) {
        client.setLevel(Level.PRO);
    }

    @Override
    public void plus(Client client) {
        throw new InvalidLevelTransitionException("Cliente não pode passar para o nível PLUS quando é ASSOCIATE");
    }

    @Override
    public void prime(Client client) {
        throw new InvalidLevelTransitionException("Cliente não pode passar para o nível PRIME quando é ASSOCIATE");
    }

}
