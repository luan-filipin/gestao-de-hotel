package com.api.gestaodehotel.service;

import com.api.gestaodehotel.dto.request.ReservaRequestDTO;
import com.api.gestaodehotel.dto.request.ReservaUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.ReservaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservaService {

    ReservaResponseDTO criarReserva(ReservaRequestDTO dto);
    ReservaResponseDTO buscarReservaPorId(Long id);
    Page<ReservaResponseDTO> buscarTodasReservas(Boolean ativo, Pageable pageable);
    void cancelarReserva(Long id);
    ReservaResponseDTO atualizarReserva(Long id, ReservaUpdateRequestDTO dto);

}