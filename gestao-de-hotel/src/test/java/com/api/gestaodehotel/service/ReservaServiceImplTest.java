package com.api.gestaodehotel.service;

import com.api.gestaodehotel.domain.Hospede;
import com.api.gestaodehotel.domain.Quarto;
import com.api.gestaodehotel.domain.Reserva;
import com.api.gestaodehotel.domain.enums.Nacionalidade;
import com.api.gestaodehotel.domain.enums.TipoQuarto;
import com.api.gestaodehotel.dto.request.ReservaRequestDTO;
import com.api.gestaodehotel.dto.request.ReservaUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.ReservaResponseDTO;
import com.api.gestaodehotel.exceptions.hospede.HospedeNaoExisteIdException;
import com.api.gestaodehotel.exceptions.quarto.QuartoInativoException;
import com.api.gestaodehotel.exceptions.quarto.QuartoNaoExistePeloIdException;
import com.api.gestaodehotel.exceptions.reserva.IdUrlDiferenteIdCorpoException;
import com.api.gestaodehotel.exceptions.reserva.ReservaExistenteException;
import com.api.gestaodehotel.exceptions.reserva.ReservaNaoExisteException;
import com.api.gestaodehotel.fixture.HospedeFixture;
import com.api.gestaodehotel.fixture.QuartoFixture;
import com.api.gestaodehotel.fixture.ReservaFixture;
import com.api.gestaodehotel.mapper.ReservaMapper;
import com.api.gestaodehotel.repository.HospedeRepository;
import com.api.gestaodehotel.repository.QuartoRepository;
import com.api.gestaodehotel.repository.ReservaRepository;
import com.api.gestaodehotel.service.impl.ReservaServiceImpl;
import com.api.gestaodehotel.service.validator.HospedeValidador;
import com.api.gestaodehotel.service.validator.QuartoValidador;
import com.api.gestaodehotel.service.validator.ReservaValidador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    private ReservaServiceImpl reservaService;
    private final ReservaMapper reservaMapper = Mappers.getMapper(ReservaMapper.class);

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private QuartoRepository quartoRepository;

    @Mock
    private HospedeRepository hospedeRepository;

    @BeforeEach
    void setUp(){
        ReservaValidador reservaValidador = new ReservaValidador(reservaRepository);
        QuartoValidador quartoValidador = new QuartoValidador(quartoRepository);
        HospedeValidador hospedeValidador = new HospedeValidador(hospedeRepository);
        reservaService = new ReservaServiceImpl(reservaRepository, reservaValidador, quartoValidador, hospedeValidador, reservaMapper);
    }

    @Test
    void deveCriarReserva() {

        ReservaRequestDTO reservaRequestDTO = ReservaFixture.criarReservaDTO(1L, 1L);
        Reserva reservaDomain = ReservaFixture.criarReservaDomain(1L);
        Quarto quartoDomain = QuartoFixture.criarQuarto(1L, 103, TipoQuarto.SOLTEIRO, 1, new BigDecimal("175.00"), true);
        Hospede hospedeDomain = HospedeFixture.criarHospedeDomain(1L, "12345678978", Nacionalidade.BRASILEIRO, true, "Teste observacao hospede");

        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quartoDomain));
        when(hospedeRepository.findById(1L)).thenReturn(Optional.of(hospedeDomain));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaDomain);

        ReservaResponseDTO resultado = reservaService.criarReserva(reservaRequestDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.quarto().numeroQuarto()).isEqualTo(103);
        assertThat(resultado.hospede().id()).isEqualTo(1L);

        verify(quartoRepository).findById(any());
        verify(hospedeRepository).findById(any());
        verify(reservaRepository).save(any());
    }

    @Test
    void deveLancarErroQuandoReservaJaExistirParaQuarto(){
        ReservaRequestDTO reservaRequestDTO = ReservaFixture.criarReservaDTO(1L, 1L);

        when(reservaRepository.existsByQuartoId(1L)).thenReturn(true);

        assertThatThrownBy(() -> reservaService.criarReserva(reservaRequestDTO))
                .isInstanceOf(ReservaExistenteException.class)
                .hasMessage("Ja existe uma reserva para esse quarto: 1");

        verify(reservaRepository).existsByQuartoId(any());
    }

    @Test
    void deveLancarErroQuandoQuartoNaoExistir(){
        ReservaRequestDTO reservaRequestDTO = ReservaFixture.criarReservaDTO(1L, 1L);

        when(quartoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservaService.criarReserva(reservaRequestDTO))
                .isInstanceOf(QuartoNaoExistePeloIdException.class)
                .hasMessage("O quarto com o ID: 1 não foi encontrado");

        verify(quartoRepository).findById(any());
    }

    @Test
    void deveLancarErroQuandoQuartoInativo(){
        ReservaRequestDTO reservaRequestDTO = ReservaFixture.criarReservaDTO(1L, 1L);
        Quarto quarto = QuartoFixture.criarQuarto(1L, 103, TipoQuarto.SOLTEIRO, 1, new BigDecimal("175.00"), false);

        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));

        assertThatThrownBy(() -> reservaService.criarReserva(reservaRequestDTO))
                .isInstanceOf(QuartoInativoException.class)
                .hasMessage("O quarto: 103 esta inativo");

        verify(quartoRepository).findById(any());
    }

    @Test
    void deveLancarErroQuandoHospedeNaoExistir(){
        ReservaRequestDTO reservaRequestDTO = ReservaFixture.criarReservaDTO(1L, 1L);
        Quarto quarto = QuartoFixture.criarQuarto(1L, 103, TipoQuarto.SOLTEIRO, 1, new BigDecimal("175.00"), true);

        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));
        when(hospedeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservaService.criarReserva(reservaRequestDTO))
                .isInstanceOf(HospedeNaoExisteIdException.class)
                .hasMessage("O hospede com o id: 1 não foi encontrado");

        verify(quartoRepository).findById(any());
        verify(hospedeRepository).findById(any());
    }

    @Test
    void deveBuscarReservaPeloId(){
        Reserva reserva = ReservaFixture.criarReservaDomain(1L);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        ReservaResponseDTO resultado = reservaService.buscarReservaPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
    }

    @Test
    void deveLancartErroQuandoReservaNaoExistir(){
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservaService.buscarReservaPorId(1L))
                .isInstanceOf(ReservaNaoExisteException.class)
                .hasMessage("A Reserva com id: 1 não foi encontrada");

        verify(reservaRepository).findById(any());
    }

    @Test
    void deveBuscarReservasQuandoAtivoTrue(){

        Pageable pageable = PageRequest.of(0, 10);
        Page<Reserva> listReserva = ReservaFixture.criaListaReservasDomainTrue();

        when(reservaRepository.findByAtivo(true, pageable)).thenReturn(listReserva);

        Page<ReservaResponseDTO> resultado = reservaService.buscarTodasReservas(true, pageable);

        assertThat(resultado).isNotNull()
                .hasSize(3)
                .allMatch(ReservaResponseDTO::ativo);

        verify(reservaRepository).findByAtivo(any(), any());
    }

    @Test
    void deveBuscarReservasQuandoAtivoFalse(){

        Pageable pageable = PageRequest.of(0, 10);
        Page<Reserva> listReserva = ReservaFixture.criaListaReservasDomainFalse();

        when(reservaRepository.findByAtivo(false, pageable)).thenReturn(listReserva);

        Page<ReservaResponseDTO> resultado = reservaService.buscarTodasReservas(false, pageable);

        assertThat(resultado).isNotNull()
                .hasSize(2)
                .allMatch(reserva -> !reserva.ativo());

        verify(reservaRepository).findByAtivo(any(), any());
    }

    @Test
    void deveBuscarTodasReservasQuandoAtivoNaoForPassado(){

        Pageable pageable = PageRequest.of(0, 10);
        Page<Reserva> listReserva = ReservaFixture.criaListaReservasDomain();

        when(reservaRepository.findByAtivo(null, pageable)).thenReturn(listReserva);

        Page<ReservaResponseDTO> resultado = reservaService.buscarTodasReservas(null, pageable);

        assertThat(resultado).isNotNull()
                .hasSize(3);
        assertThat(resultado.getContent())
                .extracting(ReservaResponseDTO::ativo)
                .contains(true, false);

        verify(reservaRepository).findByAtivo(any(), any());
    }

    @Test
    void deveCancelarReserva(){

        Reserva reserva = ReservaFixture.criarReservaDomain(1L);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        reservaService.cancelarReserva(1L);

        assertThat(reserva.getAtivo()).isFalse();

        verify(reservaRepository).findById(any());
    }

    @Test
    void deveLancarErroQuandoReservaNaoExistirParaCancelar(){
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservaService.cancelarReserva(1L))
                .isInstanceOf(ReservaNaoExisteException.class)
                .hasMessage("A Reserva com id: 1 não foi encontrada");

        verify(reservaRepository).findById(any());
    }

    @Test
    void deveAtualizarReserva() {

        Long idUrl = 1L;
        ReservaUpdateRequestDTO reservaUpdateRequestDTO = ReservaFixture.criarUpdateRequestDto(1L);
        Reserva reserva = ReservaFixture.criarReservaDomain(1L);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        ReservaResponseDTO resultado = reservaService.atualizarReserva(idUrl, reservaUpdateRequestDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.quantidadeDias()).isEqualTo(3);

        verify(reservaRepository).findById(any());
    }

    @Test
    void deveLancarErroQuandoIdUrlDiferenteDoIdCorpo(){

        Long idUrl = 2L;
        ReservaUpdateRequestDTO reservaUpdateRequestDTO = ReservaFixture.criarUpdateRequestDto(1L);

        assertThatThrownBy(() -> reservaService.atualizarReserva(idUrl, reservaUpdateRequestDTO))
                .isInstanceOf(IdUrlDiferenteIdCorpoException.class)
                .hasMessage("O id da url: 2 é diferente do id do corpo: 1");
    }

    @Test
    void deveLancarErroQuandoReservaNaoExistirParaAtualizar() {
        Long idUrl = 1L;
        ReservaUpdateRequestDTO reservaUpdateRequestDTO = ReservaFixture.criarUpdateRequestDto(1L);

        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservaService.atualizarReserva(idUrl, reservaUpdateRequestDTO))
                .isInstanceOf(ReservaNaoExisteException.class)
                .hasMessage("A Reserva com id: 1 não foi encontrada");

        verify(reservaRepository).findById(any());
    }
}
