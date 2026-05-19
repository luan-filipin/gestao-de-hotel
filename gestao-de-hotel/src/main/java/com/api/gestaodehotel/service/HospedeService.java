package com.api.gestaodehotel.service;

import com.api.gestaodehotel.dto.request.HospedeRequestDTO;
import com.api.gestaodehotel.dto.request.HospedeUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.HospedeResponseDTO;

import java.util.List;

public interface HospedeService {
    HospedeResponseDTO criarHospede(HospedeRequestDTO dto);
    HospedeResponseDTO buscarHospedePorCpf(String cpf);
    List<HospedeResponseDTO> buscaTodosOsHospedes(Boolean ativo);
    void desativarHospedePorCpf(String cpf);
    HospedeResponseDTO atualizarHospede(String cpf, HospedeUpdateRequestDTO dto);
}
