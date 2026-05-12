package com.api.gestaodehotel.dto.response;

import com.api.gestaodehotel.domain.enums.TipoQuarto;

import java.math.BigDecimal;

public record QuartoResponseDTO(
        Long id,
        Integer numeroQuarto,
        TipoQuarto tipoQuarto,
        Integer capacidade,
        BigDecimal precoPorNoite,
        String descricao,
        Boolean ativo
) {
}
