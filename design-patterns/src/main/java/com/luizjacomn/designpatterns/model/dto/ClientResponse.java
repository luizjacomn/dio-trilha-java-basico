package com.luizjacomn.designpatterns.model.dto;

import com.luizjacomn.designpatterns.model.entity.Client;
import com.luizjacomn.designpatterns.model.entity.level.ClientLevelState;

public record ClientResponse(Long id, String name, AddressResponse address, ClientLevelState.Level level) {

    public static ClientResponse from(Client client) {
        return new ClientResponse(
            client.getId(),
            client.getName(),
            new AddressResponse(
                client.getAddress().getCode(),
                client.getAddress().getLocation(),
                client.getAddress().getComplement(),
                client.getAddress().getNeighborhood(),
                client.getAddress().getCity(),
                client.getAddress().getState()
            ),
            client.getLevel()
        );
    }

}
