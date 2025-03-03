package com.luizjacomn.designpatterns.service;

import com.luizjacomn.designpatterns.model.dto.ViaCepResponse;

public interface IViaCepService {

    ViaCepResponse findByCep(String cep);

}
