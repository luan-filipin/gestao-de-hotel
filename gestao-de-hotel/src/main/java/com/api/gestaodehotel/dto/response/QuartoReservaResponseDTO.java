package com.api.gestaodehotel.dto.response;

import com.api.gestaodehotel.domain.enums.TipoQuarto;

public record QuartoReservaResponseDTO(
        Long id,
        Integer numeroQuarto,
        TipoQuarto tipoQuarto
) {
}
