package com.api.gestaodehotel.dto.request;

import com.api.gestaodehotel.domain.enums.StatusReserva;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservaRequestDTO(

        @NotNull(message = "O campo dataCheckIn é obrigatorio")
        LocalDateTime dataCheckIn,

        @NotNull(message = "O campo quantidadeDias é obrigatorio")
        Integer quantidadeDias,

        @NotNull(message = "O campo status é obrigatorio")
        StatusReserva status,

        @NotNull(message = "O campo quartoId é obrigatorio")
        Long quartoId,

        @NotNull(message = "O campo hospedeId é obrigatorio")
        Long hospedeId,

        String observacao

) {
}
