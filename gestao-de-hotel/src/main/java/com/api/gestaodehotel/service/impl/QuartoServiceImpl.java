package com.api.gestaodehotel.service.impl;

import com.api.gestaodehotel.domain.Quarto;
import com.api.gestaodehotel.dto.request.QuartoRequestDTO;
import com.api.gestaodehotel.dto.request.QuartoUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.QuartoResponseDTO;
import com.api.gestaodehotel.mapper.QuartoMapper;
import com.api.gestaodehotel.repository.QuartoRepository;
import com.api.gestaodehotel.service.QuartoService;
import com.api.gestaodehotel.service.validator.QuartoValidador;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QuartoServiceImpl implements QuartoService {

    private final QuartoRepository quartoRepository;
    private final QuartoValidador quartoValidador;
    private final QuartoMapper quartoMapper;

    @Transactional
    @Override
    public QuartoResponseDTO criarQuarto(QuartoRequestDTO dto) {
        quartoValidador.validarQuartoExistente(dto.numeroQuarto());
        Quarto quartoSalvo = quartoRepository.save(quartoMapper.toEntity(dto));
        return quartoMapper.toResponse(quartoSalvo);
    }

    @Override
    public QuartoResponseDTO buscarQuartoPorNumeroDoQuarto(Integer numeroQuarto) {
        Quarto quarto = quartoValidador.buscaQuartoOuLancarException(numeroQuarto);
        return quartoMapper.toResponse(quarto);
    }

    @Override
    public List<QuartoResponseDTO> buscarTodosQuartos(Boolean ativo) {
        List<Quarto> quartos = quartoRepository.findByAtivo(ativo);
        return quartoMapper.toListResponse(quartos);
    }

    @Transactional
    @Override
    public void desativarQuarto(Integer numeroQuarto) {
        Quarto quarto = quartoValidador.buscaQuartoOuLancarException(numeroQuarto);
        quarto.setAtivo(false);
    }

    @Transactional
    @Override
    public QuartoResponseDTO atualizarQuarto(Integer numeroQuarto, QuartoUpdateRequestDTO dto) {
        Quarto quarto = quartoValidador.buscaQuartoOuLancarException(numeroQuarto);
        quartoMapper.updateEntity(dto, quarto);
        return quartoMapper.toResponse(quarto);
    }
}
