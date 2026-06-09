package com.api.gestaodehotel.repository;

import com.api.gestaodehotel.domain.Reserva;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DBRider
@DataSet("/datasets/reserva.xml")
class ReservaRepositoryTest {

    @Autowired
    private ReservaRepository reservaRepository;

    @Test
    void deveVerificarSeQuartoJaExisteEmReserva(){
        boolean existe = reservaRepository.existsByQuartoId(1L);
        assertThat(existe).isTrue();
    }

    @Test
    void deveRetornarFalseSeQuartoNaoExistirEmReserva(){
        boolean existe = reservaRepository.existsByQuartoId(0L);
        assertThat(existe).isFalse();
    }

    @Test
    void deveBuscarReservasPeloAtivoTrue(){

        Pageable pageable = PageRequest.of(0, 10);

        Page<Reserva> reservas = reservaRepository.findByAtivo(true, pageable);
        assertThat(reservas)
                .hasSize(2)
                .extracting(Reserva::getAtivo)
                .containsOnly(true);
    }

    @Test
    void deveBuscarReservasPeloAtivoFalse(){

        Pageable pageable = PageRequest.of(0, 10);

        Page<Reserva> reservas = reservaRepository.findByAtivo(false, pageable);
        assertThat(reservas)
                .hasSize(1)
                .extracting(Reserva::getAtivo)
                .containsOnly(false);
    }

    @Test
    void deveBuscarReservasTrueEFalse(){

        Pageable pageable = PageRequest.of(0, 10);

        Page<Reserva> reservas = reservaRepository.findByAtivo(null, pageable);
        assertThat(reservas)
                .hasSize(3)
                .extracting(Reserva::getAtivo)
                .containsExactlyInAnyOrder(true, true, false);
    }
}

