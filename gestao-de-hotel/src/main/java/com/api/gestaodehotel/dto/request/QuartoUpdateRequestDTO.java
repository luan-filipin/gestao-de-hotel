package com.api.gestaodehotel.dto.request;

import com.api.gestaodehotel.domain.enums.TipoQuarto;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record QuartoUpdateRequestDTO (

        @NotNull(message = "O campo numeroQuarto é obrigadotiro")
        Integer numeroQuarto,

        TipoQuarto tipoQuarto,

        Integer capacidade,

        BigDecimal precoPorNoite,

        String descricao
){
}
