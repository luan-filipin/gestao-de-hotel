package com.api.gestaodehotel.dto.request;

import com.api.gestaodehotel.domain.enums.TipoQuarto;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record QuartoRequestDTO(
        @NotNull
        Integer numeroQuarto,

        @NotNull
        TipoQuarto tipoQuarto,

        @NotNull
        Integer capacidade,

        @NotNull
        BigDecimal precoPorNoite,

        String descricao
) {

}
