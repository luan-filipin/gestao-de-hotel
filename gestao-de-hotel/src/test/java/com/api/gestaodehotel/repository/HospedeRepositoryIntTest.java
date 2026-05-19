package com.api.gestaodehotel.repository;

import com.api.gestaodehotel.domain.Hospede;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
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
        List<Hospede> hospedeList = hospedeRepository.buscaTodosOsHospedesPorStatus(true);
        assertThat(hospedeList).hasSize(2)
                .extracting(Hospede::getAtivo)
                .containsOnly(true);
    }

    @Test
    void deveBuscarTodosOsHospedesComStatusFalse(){
        List<Hospede> hospedeList = hospedeRepository.buscaTodosOsHospedesPorStatus(false);
        assertThat(hospedeList).hasSize(1)
                .extracting(Hospede::getAtivo)
                .containsOnly(false);
    }

    @Test
    void deveBuscarTodosOsHospedesComStatusNull(){
        List<Hospede> hospedeList = hospedeRepository.buscaTodosOsHospedesPorStatus(null);
        assertThat(hospedeList).hasSize(3)
                .extracting(Hospede::getAtivo)
                .containsExactlyInAnyOrder(true, true, false);
    }
}
