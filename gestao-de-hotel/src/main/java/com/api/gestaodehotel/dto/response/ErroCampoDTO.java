package com.api.gestaodehotel.dto.response;

public record ErroCampoDTO(
        String campo,
        String mensagem
) {
}
