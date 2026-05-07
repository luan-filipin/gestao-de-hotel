package com.api.gestaodehotel.dto.request;

import com.api.gestaodehotel.domain.enums.TipoQuarto;

import java.math.BigDecimal;

public record QuartoUpdateRequestDTO (
        TipoQuarto tipoQuarto,
        Integer capacidade,
        BigDecimal precoPorNoite,
        String descricao
){
}
