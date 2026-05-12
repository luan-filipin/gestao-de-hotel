package com.api.gestaodehotel.service;

import com.api.gestaodehotel.dto.request.QuartoRequestDTO;
import com.api.gestaodehotel.dto.request.QuartoUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.QuartoResponseDTO;

import java.util.List;

public interface QuartoService {
    QuartoResponseDTO criarQuarto(QuartoRequestDTO dto);
    QuartoResponseDTO buscarQuartoPorNumeroDoQuarto(Integer numeroQuarto);
    List<QuartoResponseDTO> buscarTodosQuartos(Boolean ativo);
    void desativarQuarto(Integer numeroQuarto);
    QuartoResponseDTO atualizarQuarto(Integer numeroQuarto, QuartoUpdateRequestDTO dto);
}
