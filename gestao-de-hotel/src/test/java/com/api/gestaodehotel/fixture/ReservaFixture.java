package com.api.gestaodehotel.fixture;

import com.api.gestaodehotel.domain.Hospede;
import com.api.gestaodehotel.domain.Quarto;
import com.api.gestaodehotel.domain.Reserva;
import com.api.gestaodehotel.domain.enums.Nacionalidade;
import com.api.gestaodehotel.domain.enums.StatusReserva;
import com.api.gestaodehotel.domain.enums.TipoQuarto;
import com.api.gestaodehotel.dto.request.ReservaRequestDTO;
import com.api.gestaodehotel.dto.request.ReservaUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReservaFixture {

    public static ReservaRequestDTO criarReservaDTO(Long idQuarto, Long idHospede) {
         return new ReservaRequestDTO(
                 LocalDateTime.of(2026, 10, 6, 1, 1, 1),
                 3,
                 StatusReserva.PENDENTE,
                 idQuarto,
                 idHospede,
                 "Teste observacao"
         );
    }

    public static Reserva criarReservaDomain(Long id) {

        Quarto quarto = new Quarto(
                1L,
                103,
                TipoQuarto.CASAL,
                2,
                new BigDecimal("200.0"),
                "Teste descricao quarto",
                true
        );

        Hospede hospede = new Hospede(
                1L,
                "João teste",
                "12345678912",
                "999999999",
                "joao@gmail.com",
                LocalDate.of(1982, 5, 12),
                LocalDateTime.of(2022, 4, 12, 1, 1, 1),
                Nacionalidade.BRASILEIRO,
                true,
                "Teste Descricao hospede"
        );


        return new Reserva(
                id,
                LocalDateTime.of(2026, 8, 6, 1, 1, 1),
                LocalDateTime.of(2026, 10, 6, 1, 1, 1),
                3,
                LocalDateTime.of(2026, 10, 3, 1, 1, 1),
                new BigDecimal("278.0"),
                StatusReserva.PENDENTE,
                quarto,
                hospede,
                true,
                "Teste descricao observacao"
        );
    }

    public static Page<Reserva> criaListaReservasDomainTrue() {

        Quarto quarto = new Quarto(
                1L,
                103,
                TipoQuarto.CASAL,
                2,
                new BigDecimal("200.0"),
                "Teste descricao quarto",
                true
        );

        Hospede hospede = new Hospede(
                1L,
                "João teste",
                "12345678912",
                "999999999",
                "joao@gmail.com",
                LocalDate.of(1982, 5, 12),
                LocalDateTime.of(2022, 4, 12, 1, 1, 1),
                Nacionalidade.BRASILEIRO,
                true,
                "Teste Descricao hospede"
        );

        return new PageImpl<>(List.of(
                new Reserva(
                        1L,
                        LocalDateTime.of(2026, 8, 6, 1, 1, 1),
                        LocalDateTime.of(2026, 10, 6, 1, 1, 1),
                        3,
                        LocalDateTime.of(2026, 10, 3, 1, 1, 1),
                        new BigDecimal("278.0"),
                        StatusReserva.PENDENTE,
                        quarto,
                        hospede,
                        true,
                        "Teste descricao observacao"
                ),
                new Reserva(
                        2L,
                        LocalDateTime.of(2026, 8, 6, 1, 1, 1),
                        LocalDateTime.of(2026, 10, 6, 1, 1, 1),
                        3,
                        LocalDateTime.of(2026, 10, 3, 1, 1, 1),
                        new BigDecimal("228.0"),
                        StatusReserva.PENDENTE,
                        quarto,
                        hospede,
                        true,
                        "Teste descricao observacao"
                ),
                new Reserva(
                        3L,
                        LocalDateTime.of(2026, 8, 6, 1, 1, 1),
                        LocalDateTime.of(2026, 10, 6, 1, 1, 1),
                        3,
                        LocalDateTime.of(2026, 10, 3, 1, 1, 1),
                        new BigDecimal("828.0"),
                        StatusReserva.CONFIRMADA,
                        quarto,
                        hospede,
                        true,
                        "Teste descricao observacao"
                )
        ));
    }

    public static Page<Reserva> criaListaReservasDomainFalse() {

        Quarto quarto = new Quarto(
                1L,
                103,
                TipoQuarto.CASAL,
                2,
                new BigDecimal("200.0"),
                "Teste descricao quarto",
                true
        );

        Hospede hospede = new Hospede(
                1L,
                "João teste",
                "12345678912",
                "999999999",
                "joao@gmail.com",
                LocalDate.of(1982, 5, 12),
                LocalDateTime.of(2022, 4, 12, 1, 1, 1),
                Nacionalidade.BRASILEIRO,
                true,
                "Teste Descricao hospede"
        );

        return new PageImpl<>(List.of(
                new Reserva(
                        1L,
                        LocalDateTime.of(2026, 8, 6, 1, 1, 1),
                        LocalDateTime.of(2026, 10, 6, 1, 1, 1),
                        3,
                        LocalDateTime.of(2026, 10, 3, 1, 1, 1),
                        new BigDecimal("278.0"),
                        StatusReserva.PENDENTE,
                        quarto,
                        hospede,
                        false,
                        "Teste descricao observacao"
                ),
                new Reserva(
                        2L,
                        LocalDateTime.of(2026, 8, 6, 1, 1, 1),
                        LocalDateTime.of(2026, 10, 6, 1, 1, 1),
                        3,
                        LocalDateTime.of(2026, 10, 3, 1, 1, 1),
                        new BigDecimal("228.0"),
                        StatusReserva.PENDENTE,
                        quarto,
                        hospede,
                        false,
                        "Teste descricao observacao"
                )
        ));
    }

    public static Page<Reserva> criaListaReservasDomain() {

        Quarto quarto = new Quarto(
                1L,
                103,
                TipoQuarto.CASAL,
                2,
                new BigDecimal("200.0"),
                "Teste descricao quarto",
                true
        );

        Hospede hospede = new Hospede(
                1L,
                "João teste",
                "12345678912",
                "999999999",
                "joao@gmail.com",
                LocalDate.of(1982, 5, 12),
                LocalDateTime.of(2022, 4, 12, 1, 1, 1),
                Nacionalidade.BRASILEIRO,
                true,
                "Teste Descricao hospede"
        );

        return new PageImpl<>(List.of(
                new Reserva(
                        1L,
                        LocalDateTime.of(2026, 8, 6, 1, 1, 1),
                        LocalDateTime.of(2026, 10, 6, 1, 1, 1),
                        3,
                        LocalDateTime.of(2026, 10, 3, 1, 1, 1),
                        new BigDecimal("278.0"),
                        StatusReserva.PENDENTE,
                        quarto,
                        hospede,
                        true,
                        "Teste descricao observacao"
                ),
                new Reserva(
                        2L,
                        LocalDateTime.of(2026, 8, 6, 1, 1, 1),
                        LocalDateTime.of(2026, 10, 6, 1, 1, 1),
                        3,
                        LocalDateTime.of(2026, 10, 3, 1, 1, 1),
                        new BigDecimal("228.0"),
                        StatusReserva.PENDENTE,
                        quarto,
                        hospede,
                        false,
                        "Teste descricao observacao"
                ),
                new Reserva(
                        3L,
                        LocalDateTime.of(2026, 8, 6, 1, 1, 1),
                        LocalDateTime.of(2026, 10, 6, 1, 1, 1),
                        3,
                        LocalDateTime.of(2026, 10, 3, 1, 1, 1),
                        new BigDecimal("228.0"),
                        StatusReserva.PENDENTE,
                        quarto,
                        hospede,
                        true,
                        "Teste descricao observacao"
                )
        ));
    }

    public static ReservaUpdateRequestDTO criarUpdateRequestDto(Long id) {

        return new ReservaUpdateRequestDTO(
                id,
                LocalDateTime.of(2026, 8, 1, 1, 1, 1),
                3,
                "Teste observacao update"
        );
    }
}
