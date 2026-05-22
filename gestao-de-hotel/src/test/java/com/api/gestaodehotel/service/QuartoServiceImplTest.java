package com.api.gestaodehotel.service;

import com.api.gestaodehotel.domain.Quarto;
import com.api.gestaodehotel.domain.enums.TipoQuarto;
import com.api.gestaodehotel.dto.request.QuartoRequestDTO;
import com.api.gestaodehotel.dto.request.QuartoUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.QuartoResponseDTO;
import com.api.gestaodehotel.exceptions.NumerosDeQuartosDuplicadosException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuartoServiceImplTest {

    @Mock
    private QuartoRepository quartoRepository;

    private QuartoServiceImpl quartoService;

    private final QuartoMapper quartoMapper = Mappers.getMapper(QuartoMapper.class);

    @BeforeEach
    void setUp() {
        QuartoValidador quartoValidador = new QuartoValidador(quartoRepository);
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
    void deveCriarQuartosEmLote(){

        QuartoRequestDTO primeiroQuartoDTO = QuartoFixture.criarRequestDTO(1);
        QuartoRequestDTO segundoQuarto = QuartoFixture.criarRequestDTO(2);
        QuartoRequestDTO terceiroQuarto = QuartoFixture.criarRequestDTO(3);
        List<QuartoRequestDTO> quartosEmLoteDTO = List.of(primeiroQuartoDTO, segundoQuarto, terceiroQuarto);


        Quarto primeiroQuartoDomain = QuartoFixture.criarQuarto(1L, 1, TipoQuarto.SOLTEIRO, 1, new BigDecimal("175.00"), true);
        Quarto segundoQuartoDomain = QuartoFixture.criarQuarto(2L, 2, TipoQuarto.CASAL, 2, new BigDecimal("200.00"), true);
        Quarto terceiroQuartoDomain = QuartoFixture.criarQuarto(3L, 3, TipoQuarto.SOLTEIRO, 3, new BigDecimal("150.00"), true);
        List<Quarto> quartosDomain = List.of(primeiroQuartoDomain, segundoQuartoDomain, terceiroQuartoDomain);


        when(quartoRepository.saveAll(anyList())).thenReturn(quartosDomain);

        List<QuartoResponseDTO> resultado = quartoService.criarQuartosEmLote(quartosEmLoteDTO);

        assertThat(resultado).isNotNull().hasSize(3);
        assertThat(resultado)
                .extracting(QuartoResponseDTO::id)
                .containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    void deveLancarErroSeQuartosDuplicadosNaRequisicao(){
        List<QuartoRequestDTO> quartosEmLoteDTO = QuartoFixture.criarListaDeQuartosEmLoteDuplicados();
        assertThatThrownBy(() -> quartoService.criarQuartosEmLote(quartosEmLoteDTO))
                .isInstanceOf(NumerosDeQuartosDuplicadosException.class)
                .hasMessage("Números de quarto duplicados na requisição: [1, 2]");
    }

    @Test
    void deveLancarErroSeQuartojaExistir() {

        QuartoRequestDTO primeiroQuarto = QuartoFixture.criarRequestDTO(1);
        QuartoRequestDTO segundoQuarto = QuartoFixture.criarRequestDTO(2);
        List<QuartoRequestDTO> quartoList = List.of(primeiroQuarto, segundoQuarto);

        when(quartoRepository.existsByNumeroQuarto(1)).thenReturn(true);

        assertThatThrownBy(() -> quartoService.criarQuartosEmLote(quartoList))
                .isInstanceOf(QuartoExistenteException.class)
                .hasMessage("Ja existe um quarto com esse numero: 1");
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
        Page<Quarto> quartosAtivos = QuartoFixture.criarListaDeQuartosTrue();
        Pageable pageable = PageRequest.of(0, 10);

        when(quartoRepository.findByAtivo(ativo, pageable)).thenReturn(quartosAtivos);

        Page<QuartoResponseDTO> resultado = quartoService.buscarTodosQuartos(ativo, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado)
                .extracting(QuartoResponseDTO::id)
                .containsExactlyInAnyOrder(1L, 2L, 3L);

        verify(quartoRepository).findByAtivo(any(), any());
    }
    @Test
    void deveBuscarTodoOsQuartosInativos(){

        Boolean ativo = false;
        Page<Quarto> quartosInativos = QuartoFixture.criarListaDeQuartosFalse();
        Pageable pageable = PageRequest.of(0, 10);

        when(quartoRepository.findByAtivo(ativo, pageable)).thenReturn(quartosInativos);

        Page<QuartoResponseDTO> resultado = quartoService.buscarTodosQuartos(ativo, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado)
                .extracting(QuartoResponseDTO::id)
                .containsExactlyInAnyOrder(4L, 5L, 6L);

        verify(quartoRepository).findByAtivo(any(), any());
    }

    @Test
    void deveBuscarTodosOsQuartosQuantoAtivoForNulo(){

        List<Quarto> listaQuartos = new ArrayList<>();
        listaQuartos.addAll(QuartoFixture.criarListaDeQuartosTrue().getContent());
        listaQuartos.addAll(QuartoFixture.criarListaDeQuartosFalse().getContent());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Quarto> quartos = new PageImpl<>(listaQuartos);

        when(quartoRepository.findByAtivo(null, pageable)).thenReturn(quartos);

        Page<QuartoResponseDTO> resultado = quartoService.buscarTodosQuartos(null, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado)
                .extracting(QuartoResponseDTO::id)
                .containsExactlyInAnyOrder(1L, 2L, 3L, 4L, 5L, 6L);
        assertThat(resultado)
                .extracting(QuartoResponseDTO::ativo)
                .contains(true, true, true, false, false, false);

        verify(quartoRepository).findByAtivo(any(), any());
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
                1001,
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
                1001,
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
                1001,
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

    @Test
    void deveLancarErroQuandoNumeroQuartoForAlteradoEJaExistir(){

        Integer numeroQuarto = 1;
        QuartoUpdateRequestDTO quartoUpdateRequestDTO = QuartoFixture.criarUpdateRequestDTO(
                2,
                TipoQuarto.CASAL,
                2,
                new BigDecimal("380.00"),
                "Teste descricao QuartoUpdateRequestDTO"
        );
        Quarto quarto = QuartoFixture.criarQuarto(1L, 1, TipoQuarto.SOLTEIRO, 1, new BigDecimal("175.00"), true);

        when(quartoRepository.findByNumeroQuarto(numeroQuarto)).thenReturn(Optional.of(quarto));
        when(quartoRepository.existsByNumeroQuarto(quartoUpdateRequestDTO.numeroQuarto())).thenReturn(true);

        assertThatThrownBy(() -> quartoService.atualizarQuarto(numeroQuarto, quartoUpdateRequestDTO))
                .isInstanceOf(QuartoExistenteException.class)
                .hasMessage("Ja existe um quarto com esse numero: 2");

        verify(quartoRepository).findByNumeroQuarto(any());
        verify(quartoRepository).existsByNumeroQuarto(any());
    }
}
