package com.luizjacomn.designpatterns.model.dto;

import com.luizjacomn.designpatterns.model.entity.Address;

public record AddressResponse(
    String code,
    String location,
    String complement,
    String neighborhood,
    String city,
    String state
) {

    public Address toEntity() {
        var address = new Address();
        address.setCode(code);
        address.setLocation(location);
        address.setComplement(complement);
        address.setNeighborhood(neighborhood);
        address.setCity(city);
        address.setState(state);
        return address;
    }

}
