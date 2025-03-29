package com.luizjacomn.dira.dto;

import com.luizjacomn.dira.persistence.enumeration.Kind;

public record BoardColumnDTO(Long id, String name, Kind kind, int cardsAmount) { }
