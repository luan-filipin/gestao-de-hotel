package com.api.gestaodehotel.repository;

import com.api.gestaodehotel.domain.Hospede;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DBRider
@DataSet("/datasets/hospede.xml")
class HospedeRepositoryIntTest {

    @Autowired
    private HospedeRepository hospedeRepository;

    @Test
    void deveVerificarSeCpfExiste(){
        boolean existe = hospedeRepository.existsByCpf("12345673912");
        assertThat(existe).isTrue();
    }

    @Test
    void deveRetornarFalseSeCpfNaoExistir(){
        boolean existe = hospedeRepository.existsByCpf("12345673910");
        assertThat(existe).isFalse();
    }

    @Test
    void deveRetornarHospedeSeExistirPeloCpf(){
        Optional<Hospede> hospede = hospedeRepository.findByCpf("12345778912");
        assertThat(hospede).isPresent();
        assertThat(hospede.get().getCpf()).isEqualTo("12345778912");
    }

    @Test
    void deveRetornarVazioSeHospedeNaoExistirPeloCpf(){
        Optional<Hospede> hospede = hospedeRepository.findByCpf("22345778912");
        assertThat(hospede).isNotPresent();
    }

    @Test
    void deveBuscarTodosOsHospedesComStatusTrue(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Hospede> hospedeList = hospedeRepository.buscaTodosOsHospedesPorStatus(true, pageable);
        assertThat(hospedeList).hasSize(2)
                .extracting(Hospede::getAtivo)
                .containsOnly(true);
    }

    @Test
    void deveBuscarTodosOsHospedesComStatusFalse(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Hospede> hospedeList = hospedeRepository.buscaTodosOsHospedesPorStatus(false, pageable);
        assertThat(hospedeList).hasSize(1)
                .extracting(Hospede::getAtivo)
                .containsOnly(false);
    }

    @Test
    void deveBuscarTodosOsHospedesComStatusNull(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Hospede> hospedeList = hospedeRepository.buscaTodosOsHospedesPorStatus(null, pageable);
        assertThat(hospedeList).hasSize(3)
                .extracting(Hospede::getAtivo)
                .containsExactlyInAnyOrder(true, true, false);
    }
}
