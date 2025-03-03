package com.luizjacomn.designpatterns.model.entity.level;

import com.luizjacomn.designpatterns.model.entity.Client;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface ClientLevelState {

    void associate(Client client);

    void pro(Client client);

    void plus(Client client);

    void prime(Client client);

    @Getter
    @RequiredArgsConstructor
    enum Level {
        ASSOCIATE(new AssociateLevel()),
        PRO(new ProLevel()),
        PLUS(new PlusLevel()),
        PRIME(new PrimeLevel());

        private final ClientLevelState state;

    }

}
