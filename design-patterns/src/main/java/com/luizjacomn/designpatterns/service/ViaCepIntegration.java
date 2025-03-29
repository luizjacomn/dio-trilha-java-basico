package com.luizjacomn.designpatterns.service;

import com.luizjacomn.designpatterns.model.dto.ViaCepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viaCepIntegration", url = "${via-cep.url}")
public interface ViaCepIntegration {

    @GetMapping("/{cep}/json/")
    ViaCepResponse consultarCep(@PathVariable String cep);

}
