package com.api.gestaodehotel.dto.response;

import com.api.gestaodehotel.domain.enums.StatusReserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservaResponseDTO(

        Long id,
        LocalDateTime dataCriacao,
        LocalDateTime dataCheckIn,
        Integer quantidadeDias,
        LocalDateTime dataCheckOut,
        BigDecimal valorTotal,
        StatusReserva status,
        Boolean ativo,
        QuartoReservaResponseDTO quarto,
        HospedeReservaResponseDTO hospede,
        String observacao
) {
}
