package com.api.gestaodehotel.service;

import com.api.gestaodehotel.dto.request.QuartoRequestDTO;
import com.api.gestaodehotel.dto.request.QuartoUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.QuartoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuartoService {
    QuartoResponseDTO criarQuarto(QuartoRequestDTO dto);
    List<QuartoResponseDTO> criarQuartosEmLote(List<QuartoRequestDTO> dtos);
    QuartoResponseDTO buscarQuartoPorNumeroDoQuarto(Integer numeroQuarto);
    Page<QuartoResponseDTO> buscarTodosQuartos(Boolean ativo, Pageable page);
    void desativarQuarto(Integer numeroQuarto);
    QuartoResponseDTO atualizarQuarto(Integer numeroQuarto, QuartoUpdateRequestDTO dto);
}
