package com.luizjacomn.designpatterns.exception;

import java.io.Serial;

public class AddressNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3488485786179725270L;

    public AddressNotFoundException(String code) {
        super("Endereço não encontrado para o cep informado: " + code);
    }

}
