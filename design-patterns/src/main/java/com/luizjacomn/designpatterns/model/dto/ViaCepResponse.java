package com.luizjacomn.designpatterns.model.dto;

public record ViaCepResponse(
    String cep,
    String logradouro,
    String complemento,
    String bairro,
    String localidade,
    String uf,
    String ibge,
    String gia,
    String ddd,
    String siafi
) {

    public AddressResponse toAddressResponse() {
        return new AddressResponse(cep, logradouro, complemento, bairro, localidade, uf);
    }

}
