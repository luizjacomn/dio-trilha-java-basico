package com.luizjacomn.dira.dto;

import com.luizjacomn.dira.persistence.enumeration.Kind;

public record BoardColumnInfoDTO(Long id, int order, Kind kind) { }
