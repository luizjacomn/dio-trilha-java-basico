package com.luizjacomn.designpatterns.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClientRequest(
    @NotBlank
    String name,
    @NotBlank
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "Formato inválido, tente: 99999-999 ou 99999999")
    String code
) { }
