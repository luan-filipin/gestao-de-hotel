package com.api.gestaodehotel.service;

import com.api.gestaodehotel.dto.request.HospedeRequestDTO;
import com.api.gestaodehotel.dto.request.HospedeUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.HospedeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HospedeService {
    HospedeResponseDTO criarHospede(HospedeRequestDTO dto);
    HospedeResponseDTO buscarHospedePorCpf(String cpf);
    Page<HospedeResponseDTO> buscaTodosOsHospedes(Boolean ativo, Pageable pageable);
    void desativarHospedePorCpf(String cpf);
    HospedeResponseDTO atualizarHospede(String cpf, HospedeUpdateRequestDTO dto);
}
