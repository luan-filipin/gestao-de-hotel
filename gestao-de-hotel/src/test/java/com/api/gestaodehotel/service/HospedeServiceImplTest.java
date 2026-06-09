package com.api.gestaodehotel.service;

import com.api.gestaodehotel.domain.Hospede;
import com.api.gestaodehotel.domain.enums.Nacionalidade;
import com.api.gestaodehotel.dto.request.HospedeRequestDTO;
import com.api.gestaodehotel.dto.request.HospedeUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.HospedeResponseDTO;
import com.api.gestaodehotel.exceptions.hospede.HospedeJaExisteException;
import com.api.gestaodehotel.exceptions.hospede.HospedeJainativoPeloCpfException;
import com.api.gestaodehotel.exceptions.hospede.HospedeNaoExisteCpfException;
import com.api.gestaodehotel.fixture.HospedeFixture;
import com.api.gestaodehotel.mapper.HospedeMapper;
import com.api.gestaodehotel.repository.HospedeRepository;
import com.api.gestaodehotel.service.impl.HospedeServiceImpl;
import com.api.gestaodehotel.service.validator.HospedeValidador;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HospedeServiceImplTest {

    private HospedeServiceImpl hospedeService;
    private final HospedeMapper hospedeMapper = Mappers.getMapper(HospedeMapper.class);

    @Mock
    private HospedeRepository hospedeRepository;

    @BeforeEach
    void setUp(){
        HospedeValidador hospedeValidador = new HospedeValidador(hospedeRepository);
        hospedeService = new HospedeServiceImpl(hospedeRepository, hospedeValidador, hospedeMapper);
    }

    @Test
    void deveCriarHospede(){

        HospedeRequestDTO hospedeDTO = HospedeFixture.criarHospedeDTO("12345678912");
        Hospede hospede = HospedeFixture.criarHospedeDomain(1L,  "12345678912", Nacionalidade.BRASILEIRO,true, "Teste observação");

        when(hospedeRepository.save(any(Hospede.class))).thenReturn(hospede);

        HospedeResponseDTO resultado = hospedeService.criarHospede(hospedeDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.cpf()).isEqualTo("12345678912");
        assertThat(resultado.nacionalidade()).isEqualTo(Nacionalidade.BRASILEIRO);
        assertThat(resultado.ativo()).isTrue();
        assertThat(resultado.observacao()).isEqualTo("Teste observação");

        verify(hospedeRepository).save(any());
    }

    @Test
    void deveLancarErroSeHospedeJaExistir(){

        HospedeRequestDTO hospedeDTO = HospedeFixture.criarHospedeDTO("12345678912");

        when(hospedeRepository.existsByCpf(hospedeDTO.cpf())).thenReturn(true);

        assertThatThrownBy(() -> hospedeService.criarHospede(hospedeDTO))
                .isInstanceOf(HospedeJaExisteException.class)
                .hasMessage("Ja existe um hospede com esse cpf: 12345678912");

        verify(hospedeRepository).existsByCpf(any());
        verify(hospedeRepository, never()).save(any());
    }

    @Test
    void deveBuscarHospedePeloCpf(){

        String cpf = "12345678912";
        Hospede hospede = HospedeFixture.criarHospedeDomain(1L,  "12345678912", Nacionalidade.BRASILEIRO,true, "Teste observação");

        when(hospedeRepository.findByCpf(cpf)).thenReturn(Optional.of(hospede));

        HospedeResponseDTO resultado = hospedeService.buscarHospedePorCpf(cpf);

        assertThat(resultado).isNotNull();
        assertThat(resultado.cpf()).isEqualTo("12345678912");
    }

    @Test
    void deveLancarErroSeHospedeNaoExistirPeloCpf(){

        when(hospedeRepository.findByCpf("12345678912")).thenReturn(Optional.empty());

        assertThatThrownBy(()-> hospedeService.buscarHospedePorCpf("12345678912"))
                .isInstanceOf(HospedeNaoExisteCpfException.class)
                .hasMessage("Não existe um hospede com esse cpf: 12345678912");

        verify(hospedeRepository).findByCpf(any());
    }

    @Test
    void deveBuscarHospedesComStatusTrue(){
        Pageable pageable = PageRequest.of(0, 10);

        Page<Hospede> hospedeList = HospedeFixture.criarListHospedeDomainTrue();
        when(hospedeRepository.buscaTodosOsHospedesPorStatus(true, pageable)).thenReturn(hospedeList);

        Page<HospedeResponseDTO> resultado = hospedeService.buscaTodosOsHospedes(true, pageable);

        assertThat(resultado)
                .isNotNull()
                .hasSize(3)
                .allMatch(HospedeResponseDTO::ativo);

        verify(hospedeRepository).buscaTodosOsHospedesPorStatus(any(), any());
    }

    @Test
    void deveBuscarHospedesComStatusFalse(){

        Pageable pageable = PageRequest.of(0, 10);
        Page<Hospede> hospedeList = HospedeFixture.criarListHospedeDomainFalse();

        when(hospedeRepository.buscaTodosOsHospedesPorStatus(false, pageable)).thenReturn(hospedeList);

        Page<HospedeResponseDTO> resultado = hospedeService.buscaTodosOsHospedes(false, pageable);

        assertThat(resultado)
                .isNotNull()
                .hasSize(2)
                .allMatch(hospede -> !hospede.ativo());

        verify(hospedeRepository).buscaTodosOsHospedesPorStatus(any(), any());
    }

    @Test
    void deveBuscarTodosOsHospedesQuandoStatusNaoForPassado(){
        Pageable pageable = PageRequest.of(0, 10);

        List<Hospede> listHospede = new ArrayList<>();
        listHospede.addAll(HospedeFixture.criarListHospedeDomainTrue().getContent());
        listHospede.addAll(HospedeFixture.criarListHospedeDomainFalse().getContent());

        Page<Hospede> hospedes = new PageImpl<>(listHospede);

        when(hospedeRepository.buscaTodosOsHospedesPorStatus(null, pageable)).thenReturn(hospedes);

        Page<HospedeResponseDTO> resultado = hospedeService.buscaTodosOsHospedes(null, pageable);

        assertThat(resultado)
                .isNotNull()
                .hasSize(5);

        verify(hospedeRepository).buscaTodosOsHospedesPorStatus(any(), any());
    }

    @Test
    void deveDesativarHospedePeloCpf(){

        String cpf = "12345678912";
        Hospede hospede = HospedeFixture.criarHospedeDomain(1L,  "12345678912", Nacionalidade.BRASILEIRO,true, "Teste observação");

        when(hospedeRepository.findByCpf(cpf)).thenReturn(Optional.of(hospede));

        hospedeService.desativarHospedePorCpf(cpf);

        assertThat(hospede.getAtivo()).isFalse();

        verify(hospedeRepository).findByCpf(any());
    }

    @Test
    void deveLancarErroAoDesativarSeHospedeNaoExistirPeloCpf(){

        when(hospedeRepository.findByCpf("12345678912")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hospedeService.desativarHospedePorCpf("12345678912"))
                .isInstanceOf(HospedeNaoExisteCpfException.class)
                .hasMessage("Não existe um hospede com esse cpf: 12345678912");

        verify(hospedeRepository).findByCpf(any());
    }

    @Test
    void deveLancarErroSeHospedeJaDesativadoPeloCpf(){

        Hospede hospede = HospedeFixture.criarHospedeDomain(1L,  "12345678912", Nacionalidade.BRASILEIRO,false, "Teste observação");

        when(hospedeRepository.findByCpf("12345678912")).thenReturn(Optional.of(hospede));

        assertThatThrownBy(() -> hospedeService.desativarHospedePorCpf("12345678912"))
                .isInstanceOf(HospedeJainativoPeloCpfException.class)
                .hasMessage("O hospede com cpf: 12345678912 ja esta inativo");

        verify(hospedeRepository).findByCpf(any());
    }

    @Test
    void deveAtualizarHospede(){

        String cpf = "12345678912";

        Hospede hospede = HospedeFixture.criarHospedeDomain(1L,  "12345678918", Nacionalidade.BRASILEIRO,true, "Teste observação");
        HospedeUpdateRequestDTO hospedeUpdateDTO = HospedeFixture.criarUpdateRequestDTO(
                "Maria teste",
                "12345678912",
                "44999999945",
                "maria@gmail.com",
                LocalDate.of(2021, 2, 12),
                Nacionalidade.ESTRANGEIRO,
                "Teste descricao Maria"
        );

        when(hospedeRepository.findByCpf(cpf)).thenReturn(Optional.of(hospede));

        HospedeResponseDTO resultado = hospedeService.atualizarHospede(cpf, hospedeUpdateDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.nome()).isEqualTo("Maria teste");
        assertThat(resultado.cpf()).isEqualTo("12345678912");
        assertThat(resultado.telefone()).isEqualTo("44999999945");
        assertThat(resultado.email()).isEqualTo("maria@gmail.com");
        assertThat(resultado.dataNascimento()).isEqualTo(LocalDate.of(2021, 2, 12));
        assertThat(resultado.nacionalidade()).isEqualTo(Nacionalidade.ESTRANGEIRO);
        assertThat(resultado.observacao()).isEqualTo("Teste descricao Maria");
    }
}
