package com.luizjacomn.designpatterns.exception;

import java.io.Serial;

public class ClientNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7681886402275228713L;

    public ClientNotFoundException(Long id) {
        super("Cliente não encontrado para o id informado: " + id);
    }

}
