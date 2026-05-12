package com.api.gestaodehotel.service;

import com.api.gestaodehotel.domain.Quarto;
import com.api.gestaodehotel.domain.enums.TipoQuarto;
import com.api.gestaodehotel.dto.request.QuartoRequestDTO;
import com.api.gestaodehotel.dto.request.QuartoUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.QuartoResponseDTO;
import com.api.gestaodehotel.exceptions.QuartoEstaInativoException;
import com.api.gestaodehotel.exceptions.QuartoExistenteException;
import com.api.gestaodehotel.exceptions.QuartoNaoExisteException;
import com.api.gestaodehotel.fixture.QuartoFixture;
import com.api.gestaodehotel.mapper.QuartoMapper;
import com.api.gestaodehotel.repository.QuartoRepository;
import com.api.gestaodehotel.service.impl.QuartoServiceImpl;
import com.api.gestaodehotel.service.validator.QuartoValidador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuartoServiceImplTest {

    private QuartoValidador quartoValidador;

    @Mock
    private QuartoRepository quartoRepository;

    private QuartoServiceImpl quartoService;

    private final QuartoMapper quartoMapper = Mappers.getMapper(QuartoMapper.class);

    @BeforeEach
    void setUp() {
        quartoValidador = new QuartoValidador(quartoRepository);
        quartoService = new QuartoServiceImpl(quartoRepository, quartoValidador, quartoMapper);
    }

    @Test
    void deveCriarQuarto() {
        QuartoRequestDTO quartoRequestDTO = QuartoFixture.criarRequestDTO(1);
        Quarto quartoSalvo = QuartoFixture.criarQuarto(1L, 1, TipoQuarto.SOLTEIRO, 1, new BigDecimal("175.00"), true);

        when(quartoRepository.save(any(Quarto.class))).thenReturn(quartoSalvo);

        QuartoResponseDTO resultado = quartoService.criarQuarto(quartoRequestDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.numeroQuarto()).isEqualTo(1);
        assertThat(resultado.tipoQuarto()).isEqualTo(TipoQuarto.SOLTEIRO);
        assertThat(resultado.capacidade()).isEqualTo(1);
        assertThat(resultado.precoPorNoite()).isEqualByComparingTo("175.00");
        assertThat(resultado.descricao()).isEqualTo("Teste descricao");
        assertThat(resultado.ativo()).isTrue();

        verify(quartoRepository).save(any());
    }

    @Test
    void deveLancarExceptionSeQuartoJaExistir(){
        QuartoRequestDTO quartoRequestDTO = QuartoFixture.criarRequestDTO(1);

        when(quartoRepository.existsByNumeroQuarto(1)).thenReturn(true);

        assertThatThrownBy(() -> quartoService.criarQuarto(quartoRequestDTO))
                .isInstanceOf(QuartoExistenteException.class)
                .hasMessage("Ja existe um quarto com esse numero: 1");

        verify(quartoRepository).existsByNumeroQuarto(1);
        verify(quartoRepository, never()).save(any());
    }

    @Test
    void deveBuscarQuartoPeloNumeroDoQuarto(){

        Integer numeroQuarto = 1;
        Quarto quarto = QuartoFixture.criarQuarto(1L, 1, TipoQuarto.SOLTEIRO, 1, new BigDecimal("175.00"), true);

        when(quartoRepository.findByNumeroQuarto(numeroQuarto)).thenReturn(Optional.of(quarto));

        QuartoResponseDTO resultado = quartoService.buscarQuartoPorNumeroDoQuarto(numeroQuarto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.numeroQuarto()).isEqualTo(1);

        verify(quartoRepository).findByNumeroQuarto(1);
    }

    @Test
    void deveLancarExceptionSeQuatoNaoExistir(){

        when(quartoRepository.findByNumeroQuarto(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quartoService.buscarQuartoPorNumeroDoQuarto(1))
                .isInstanceOf(QuartoNaoExisteException.class)
                .hasMessage("O quarto 1 não foi encontrado");

        verify(quartoRepository).findByNumeroQuarto(1);
    }

    @Test
    void deveBuscarTodosOsQuartosAtivos(){

        Boolean ativo = true;
        List<Quarto> quartosAtivos = QuartoFixture.criarListaDeQuartosTrue();

        when(quartoRepository.findByAtivo(ativo)).thenReturn(quartosAtivos);

        List<QuartoResponseDTO> resultado = quartoService.buscarTodosQuartos(ativo);

        assertThat(resultado).isNotNull();
        assertThat(resultado)
                .extracting(QuartoResponseDTO::id)
                .containsExactlyInAnyOrder(1L, 2L, 3L);

        verify(quartoRepository).findByAtivo(true);
    }
    @Test
    void deveBuscarTodoOsQuartosInativos(){

        Boolean ativo = false;
        List<Quarto> quartosInativos = QuartoFixture.criarListaDeQuartosFalse();

        when(quartoRepository.findByAtivo(ativo)).thenReturn(quartosInativos);

        List<QuartoResponseDTO> resultado = quartoService.buscarTodosQuartos(ativo);

        assertThat(resultado).isNotNull();
        assertThat(resultado)
                .extracting(QuartoResponseDTO::id)
                .containsExactlyInAnyOrder(4L, 5L, 6L);

        verify(quartoRepository).findByAtivo(false);
    }

    @Test
    void deveBuscarTodosOsQuartosQuantoAtivoForNulo(){

        List<Quarto> quartos = new ArrayList<>();
        quartos.addAll(QuartoFixture.criarListaDeQuartosTrue());
        quartos.addAll(QuartoFixture.criarListaDeQuartosFalse());

        when(quartoRepository.findByAtivo(null)).thenReturn(quartos);

        List<QuartoResponseDTO> resultado = quartoService.buscarTodosQuartos(null);

        assertThat(resultado).isNotNull();
        assertThat(resultado)
                .extracting(QuartoResponseDTO::id)
                .containsExactlyInAnyOrder(1L, 2L, 3L, 4L, 5L, 6L);
        assertThat(resultado)
                .extracting(QuartoResponseDTO::ativo)
                .contains(true, true, true, false, false, false);

        verify(quartoRepository).findByAtivo(null);
    }

    @Test
    void deveDesativarQuarto(){

        Integer numeroQuarto = 1;

        Quarto quarto = QuartoFixture.criarQuarto(1L, 1, TipoQuarto.SOLTEIRO, 1, new BigDecimal("175.00"), true);
        when(quartoRepository.findByNumeroQuarto(numeroQuarto)).thenReturn(Optional.of(quarto));

        quartoService.desativarQuarto(numeroQuarto);

        assertThat(quarto.getAtivo()).isFalse();

        verify(quartoRepository).findByNumeroQuarto(1);
    }


    @Test
    void deveLancarExceptionSeQuartoNaoExistir(){

        Integer numeroQuarto = 1;

        when(quartoRepository.findByNumeroQuarto(numeroQuarto)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quartoService.desativarQuarto(numeroQuarto))
                .isInstanceOf(QuartoNaoExisteException.class)
                .hasMessage("O quarto 1 não foi encontrado");

        verify(quartoRepository).findByNumeroQuarto(numeroQuarto);
    }

    @Test
    void deveLancartExceptionSeQuartoJaEstiverInativo(){
        Integer numeroQuarto = 1;
        Quarto quarto = QuartoFixture.criarQuarto(1L, 1, TipoQuarto.SOLTEIRO, 1, new BigDecimal("175.00"), false);

        when(quartoRepository.findByNumeroQuarto(numeroQuarto)).thenReturn(Optional.of(quarto));

        assertThatThrownBy(() -> quartoService.desativarQuarto(numeroQuarto))
                .isInstanceOf(QuartoEstaInativoException.class)
                .hasMessage("O quarto 1 ja esta inativo");
    }

    @Test
    void deveAtualizarQuarto(){

        Integer numeroQuarto = 1;
        QuartoUpdateRequestDTO quartoUpdateRequestDTO = QuartoFixture.criarUpdateRequestDTO(
                TipoQuarto.CASAL,
                2,
                new BigDecimal("380.00"),
                "Teste descricao QuartoUpdateRequestDTO"
        );
        Quarto quarto = QuartoFixture.criarQuarto(1L, 1, TipoQuarto.SOLTEIRO, 1, new BigDecimal("175.00"), true);

        when(quartoRepository.findByNumeroQuarto(numeroQuarto)).thenReturn(Optional.of(quarto));
        QuartoResponseDTO resultado = quartoService.atualizarQuarto(numeroQuarto, quartoUpdateRequestDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.tipoQuarto()).isEqualTo(TipoQuarto.CASAL);
        assertThat(resultado.capacidade()).isEqualTo(2);
        assertThat(resultado.precoPorNoite()).isEqualByComparingTo("380.00");
        assertThat(resultado.descricao()).isEqualTo("Teste descricao QuartoUpdateRequestDTO");
        assertThat(resultado.ativo()).isTrue();

        verify(quartoRepository).findByNumeroQuarto(1);
    }

    @Test
    void deveAtualizarQuartoIgnorandoCamposNulos(){
        Integer numeroQuarto = 1;
        QuartoUpdateRequestDTO quartoUpdateRequestDTO = new QuartoUpdateRequestDTO(
                TipoQuarto.CASAL,
                2,
                null,
                "Teste descricao QuartoUpdateRequestDTO"
        );
        Quarto quarto = QuartoFixture.criarQuarto(1L, 1, TipoQuarto.SOLTEIRO, 1, new BigDecimal("175.00"), true);

        when(quartoRepository.findByNumeroQuarto(numeroQuarto)).thenReturn(Optional.of(quarto));

        QuartoResponseDTO resultado = quartoService.atualizarQuarto(numeroQuarto, quartoUpdateRequestDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.tipoQuarto()).isEqualTo(TipoQuarto.CASAL);
        assertThat(resultado.capacidade()).isEqualTo(2);
        assertThat(resultado.precoPorNoite()).isEqualByComparingTo("175.00");
        assertThat(resultado.descricao()).isEqualTo("Teste descricao QuartoUpdateRequestDTO");

        verify(quartoRepository).findByNumeroQuarto(1);
    }

    @Test
    void deveLancarExceptionSeQuartoNaoExistirParaAtualizar(){

        Integer numeroQuarto = 1;
        QuartoUpdateRequestDTO quartoUpdateRequestDTO = QuartoFixture.criarUpdateRequestDTO(
                TipoQuarto.CASAL,
                2,
                new BigDecimal("380.00"),
                "Teste descricao QuartoUpdateRequestDTO"
        );

        when(quartoRepository.findByNumeroQuarto(numeroQuarto)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quartoService.atualizarQuarto(numeroQuarto, quartoUpdateRequestDTO))
                .isInstanceOf(QuartoNaoExisteException.class)
                .hasMessage("O quarto 1 não foi encontrado");

        verify(quartoRepository).findByNumeroQuarto(numeroQuarto);
    }
}
