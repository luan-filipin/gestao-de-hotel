package com.api.gestaodehotel.repository;

import com.api.gestaodehotel.domain.Quarto;
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
@DataSet("datasets/quarto.xml")
public class QuartoRepositoryIntTest {

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
        List<Quarto> quartos = quartoRepository.findByAtivo(true);
        assertThat(quartos).hasSize(2);
    }

    @Test
    void deveBuscarTodosOsQuartosComStatusFalse(){
        List<Quarto> quartos = quartoRepository.findByAtivo(false);
        assertThat(quartos).hasSize(1);
    }

    @Test
    void deveBuscarTodosOsQuartosComStatusNull(){
        List<Quarto> quartos = quartoRepository.findByAtivo(null);
        assertThat(quartos).hasSize(3);
    }
}
