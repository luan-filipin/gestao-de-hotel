package com.api.gestaodehotel.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ErroResponseDTO {

    private String mensagem;
    private int status;
    private String path;
    private Instant timestamp;
    private List<ErroCampoDTO> erros;

    public ErroResponseDTO(String mensagem, int status, String path) {
        this.mensagem = mensagem;
        this.status = status;
        this.path = path;
        this.timestamp = Instant.now();
    }

    public ErroResponseDTO(String message, int status, String path, List<ErroCampoDTO> erros) {
        this(message, status, path);
        this.erros = erros;
    }
}
