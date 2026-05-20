package com.api.gestaodehotel.dto.request;

import com.api.gestaodehotel.domain.enums.TipoQuarto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record QuartoRequestDTO(
        @NotNull(message = "O numero do quarto é obrigatorio.")
        Integer numeroQuarto,

        @NotNull(message = "O tipo do quarto é obrigatorio.")
        TipoQuarto tipoQuarto,

        @NotNull(message = "A capacidade do quarto é obrigado.")
        Integer capacidade,

        @NotNull(message = "O preco do quarto é obrigatorio.")
        BigDecimal precoPorNoite,

        @Size(max = 300)
        String descricao
) {

}
