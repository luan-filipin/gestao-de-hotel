package com.api.gestaodehotel.repository;

import com.api.gestaodehotel.domain.Quarto;
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
@DataSet("datasets/quarto.xml")
class QuartoRepositoryIntTest {

    @Autowired
    private QuartoRepository quartoRepository;

    @Test
    void deveVerificarSeQuartoExiste(){
        boolean existe = quartoRepository.existsByNumeroQuarto(1);
        assertThat(existe).isTrue();
    }

    @Test
    void deveRetornarFalseSeQuartoNaoExistir(){
        boolean existe = quartoRepository.existsByNumeroQuarto(0);
        assertThat(existe).isFalse();
    }

    @Test
    void deveBuscarQuartoPorNumero(){
        Optional<Quarto> quarto = quartoRepository.findByNumeroQuarto(2);
        assertThat(quarto).isPresent();
        assertThat(quarto.get().getNumeroQuarto()).isEqualTo(2);
    }

    @Test
    void deveRetornarOptionalVazioSeQuartoNaoExistir(){
        Optional<Quarto> quarto = quartoRepository.findByNumeroQuarto(0);
        assertThat(quarto).isEmpty();
    }

    @Test
    void deveBuscarTodosOsQuartosComStatusTrue(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Quarto> quartos = quartoRepository.findByAtivo(true, pageable);
        assertThat(quartos).hasSize(2)
                .extracting(Quarto::getAtivo)
                .containsOnly(true);
    }

    @Test
    void deveBuscarTodosOsQuartosComStatusFalse(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Quarto> quartos = quartoRepository.findByAtivo(false, pageable);
        assertThat(quartos).hasSize(1)
                .extracting(Quarto::getAtivo)
                .containsOnly(false);
    }

    @Test
    void deveBuscarTodosOsQuartosComStatusNull(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Quarto> quartos = quartoRepository.findByAtivo(null, pageable);
        assertThat(quartos).hasSize(3)
                .extracting(Quarto::getAtivo)
                .containsExactlyInAnyOrder(true, true, false);
    }
}
