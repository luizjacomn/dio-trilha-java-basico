package com.luizjacomn.designpatterns.service;

import com.luizjacomn.designpatterns.exception.AddressNotFoundException;
import com.luizjacomn.designpatterns.model.dto.ViaCepResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViaCepService implements IViaCepService {

    private final ViaCepIntegration viaCepIntegration;

    @Override
    public ViaCepResponse findByCep(String cep) {
        var onlyNumbers = cep.replace("\\D", "");

        var response = viaCepIntegration.consultarCep(onlyNumbers);

        if (Objects.isNull(response.cep())) {
            throw new AddressNotFoundException(cep);
        }

        return response;
    }

}
