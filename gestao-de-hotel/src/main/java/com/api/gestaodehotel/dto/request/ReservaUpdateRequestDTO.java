package com.api.gestaodehotel.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservaUpdateRequestDTO(
        @NotNull(message = "O Id da reserva é obrigatorio.")
        Long id,
        LocalDateTime dataCheckIn,
        Integer quantidadeDias,
        String observacao

) {
}
