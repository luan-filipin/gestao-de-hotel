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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return quartoMapper.toDTO(quartoSalvo);
    }

    @Transactional
    @Override
    public List<QuartoResponseDTO> criarQuartosEmLote(List<QuartoRequestDTO> dtos) {

        List<Integer> numerosDosQuartos = dtos.stream()
                .map(QuartoRequestDTO::numeroQuarto)
                .toList();

        quartoValidador.validarNumerosDeQuartosDuplicadosNaRequisicao(numerosDosQuartos);
        numerosDosQuartos.forEach(quartoValidador::validarQuartoExistente);

        List<Quarto> quartosSalvos = quartoRepository.saveAll(quartoMapper.toEntityList(dtos));

        return quartoMapper.toListResponse(quartosSalvos);
    }

    @Override
    public QuartoResponseDTO buscarQuartoPorNumeroDoQuarto(Integer numeroQuarto) {
        Quarto quarto = quartoValidador.buscaQuartoOuLancarException(numeroQuarto);
        return quartoMapper.toDTO(quarto);
    }

    @Override
    public Page<QuartoResponseDTO> buscarTodosQuartos(Boolean ativo, Pageable page) {
        Page<Quarto> quartos = quartoRepository.findByAtivo(ativo, page);
        return quartoMapper.toPageListResponse(quartos);
    }

    @Transactional
    @Override
    public void desativarQuarto(Integer numeroQuarto) {
        Quarto quarto = quartoValidador.buscaQuartoEValidaSeInativoOuLancaException(numeroQuarto);
        quarto.setAtivo(false);
    }

    @Transactional
    @Override
    public QuartoResponseDTO atualizarQuarto(Integer numeroQuarto, QuartoUpdateRequestDTO dto) {
        Quarto quarto = quartoValidador.buscaQuartoOuLancarException(numeroQuarto);
        quartoValidador.ValidarSeNovoNumeroQuartoJaExiste(numeroQuarto, dto.numeroQuarto());
        quartoMapper.updateEntity(dto, quarto);
        return quartoMapper.toDTO(quarto);
    }
}
