package com.api.gestaodehotel.service.impl;

import com.api.gestaodehotel.domain.Hospede;
import com.api.gestaodehotel.dto.request.HospedeRequestDTO;
import com.api.gestaodehotel.dto.request.HospedeUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.HospedeResponseDTO;
import com.api.gestaodehotel.mapper.HospedeMapper;
import com.api.gestaodehotel.repository.HospedeRepository;
import com.api.gestaodehotel.service.HospedeService;
import com.api.gestaodehotel.service.validator.HospedeValidador;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HospedeServiceImpl implements HospedeService {

    private final HospedeRepository hospedeRepository;
    private final HospedeValidador hospedeValidador;
    private final HospedeMapper hospedeMapper;

    @Transactional
    @Override
    public HospedeResponseDTO criarHospede(HospedeRequestDTO dto) {
        hospedeValidador.validaSeHospedeExiste(dto.cpf());
        Hospede hospede = hospedeRepository.save(hospedeMapper.toEntity(dto));
        return hospedeMapper.toDTO(hospede);
    }

    @Override
    public HospedeResponseDTO buscarHospedePorCpf(String cpf) {
        Hospede hospede = hospedeValidador.validaSeHospedeNaoExistePeloCpf(cpf);
        return hospedeMapper.toDTO(hospede);
    }

    @Override
    public List<HospedeResponseDTO> buscaTodosOsHospedes(Boolean ativo) {
        List<Hospede> hospedes = hospedeRepository.buscaTodosOsHospedesPorStatus(ativo);
        return hospedeMapper.toResponseList(hospedes);
    }

    @Transactional
    @Override
    public void desativarHospedePorCpf(String cpf) {
        Hospede hospede = hospedeValidador.validaSeHospedeExisteEInativoPeloCpf(cpf);
        hospede.setAtivo(false);
    }

    @Override
    public HospedeResponseDTO atualizarHospede(String cpf, HospedeUpdateRequestDTO dto) {
        Hospede hospede = hospedeValidador.validaSeHospedeNaoExistePeloCpf(cpf);
        hospedeValidador.validarSeNovoCpfJaExiste(cpf, dto.cpf());
        hospedeMapper.updateEntity(dto, hospede);
        return hospedeMapper.toDTO(hospede);
    }
}
