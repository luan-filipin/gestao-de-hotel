package com.api.gestaodehotel.service.impl;

import com.api.gestaodehotel.domain.Hospede;
import com.api.gestaodehotel.domain.Quarto;
import com.api.gestaodehotel.domain.Reserva;
import com.api.gestaodehotel.dto.request.ReservaRequestDTO;
import com.api.gestaodehotel.dto.request.ReservaUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.ReservaResponseDTO;
import com.api.gestaodehotel.mapper.ReservaMapper;
import com.api.gestaodehotel.repository.ReservaRepository;
import com.api.gestaodehotel.service.ReservaService;
import com.api.gestaodehotel.service.validator.HospedeValidador;
import com.api.gestaodehotel.service.validator.QuartoValidador;
import com.api.gestaodehotel.service.validator.ReservaValidador;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaValidador reservaValidador;
    private final QuartoValidador quartoValidador;
    private final HospedeValidador hospedeValidador;
    private final ReservaMapper reservaMapper;



    @Transactional
    @Override
    public ReservaResponseDTO criarReserva(ReservaRequestDTO dto) {

        reservaValidador.validaSeReservaJaExisteParaQuarto(dto.quartoId());
        Quarto quartoReserva = quartoValidador.buscaQuartoAtivoPorId(dto.quartoId());
        Hospede hospedeReserva = hospedeValidador.validaHospedeExisteENaoInativo(dto.hospedeId());

        Reserva reserva = new Reserva();
        reserva.setDataCheckIn(dto.dataCheckIn());
        reserva.setQuantidadeDias(dto.quantidadeDias());
        reserva.setQuarto(quartoReserva);
        reserva.setHospede(hospedeReserva);
        reserva.setStatus(dto.status());
        reserva.setObservacao(dto.observacao());
        reserva.calculaDataCheckOut();
        reserva.calculaValorTotal();

        return reservaMapper.toDTO(reservaRepository.save(reserva));
    }

    @Override
    public ReservaResponseDTO buscarReservaPorId(Long id) {
        return reservaMapper.toDTO(reservaValidador.validaSeReservaExiste(id));
    }

    @Override
    public Page<ReservaResponseDTO> buscarTodasReservas(Boolean ativo, Pageable pageable) {
        Page<Reserva> reservas = reservaRepository.findByAtivo(ativo, pageable);
        return reservaMapper.toDTOReservas(reservas);
    }

    @Transactional
    @Override
    public void cancelarReserva(Long id) {
        Reserva reserva = reservaValidador.validaSeReservaExiste(id);
        reserva.setAtivo(false);
    }

    @Transactional
    @Override
    public ReservaResponseDTO atualizarReserva(Long id, ReservaUpdateRequestDTO dto) {
        reservaValidador.validaSeIdDoCorpoEIgualAoIdUrl(id, dto.id());
        Reserva reserva = reservaValidador.validaSeReservaExiste(id);

        reserva.setDataCheckIn(dto.dataCheckIn());
        reserva.setQuantidadeDias(dto.quantidadeDias());
        reserva.setObservacao(dto.observacao());

        reserva.calculaDataCheckOut();
        reserva.calculaValorTotal();

        return reservaMapper.toDTO(reserva);
    }
}
