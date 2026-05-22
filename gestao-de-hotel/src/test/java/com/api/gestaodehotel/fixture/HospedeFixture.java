package com.api.gestaodehotel.fixture;

import com.api.gestaodehotel.domain.Hospede;
import com.api.gestaodehotel.domain.enums.Nacionalidade;
import com.api.gestaodehotel.dto.request.HospedeRequestDTO;
import com.api.gestaodehotel.dto.request.HospedeUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class HospedeFixture {

    public static HospedeRequestDTO criarHospedeDTO(String cpf){
        return new HospedeRequestDTO(
                "Joao Teste",
                cpf,
                "44999999999",
                "joao@gmail.com",
                LocalDate.of(1992, 5, 2),
                Nacionalidade.BRASILEIRO,
                "Teste observação"
        );
    }

    public static Hospede criarHospedeDomain(
            Long id,
            String cpf,
            Nacionalidade nacionalidade,
            Boolean ativo,
            String observacao){

        return new Hospede(
                id,
                "Joao Teste",
                cpf,
                "44999999999",
                "joao@gmail.com",
                LocalDate.of(1992, 5, 2),
                LocalDateTime.of(2026, 5, 15, 1, 1, 1),
                nacionalidade,
                ativo,
                observacao
        );
    }

    public static Page<Hospede> criarListHospedeDomainTrue(){
        return new PageImpl<>(List.of(
                new Hospede(1L,  "Joao Teste", "1234567891234", "44999999999", "joao@gmail.com", LocalDate.of(2025, 5, 1), LocalDateTime.of(2026, 5, 18, 1, 1, 1), Nacionalidade.BRASILEIRO, true, "Teste observacao Joao"),
                new Hospede(2L, "Maria Teste", "1234567891243", "44999999998", "maria@gmail.com", LocalDate.of(2021, 9, 4), LocalDateTime.of(2025, 7, 11, 1, 1, 1), Nacionalidade.BRASILEIRO, true, "Teste observacao Maria"),
                new Hospede(3L, "Thiago Teste", "1234567891257", "44999999945", "thiago@gmail.com", LocalDate.of(2019, 11, 17), LocalDateTime.of(2023, 9, 7, 1, 1, 1), Nacionalidade.ESTRANGEIRO, true, "Teste observacao Thiago")
        ));
    }

    public static Page<Hospede> criarListHospedeDomainFalse(){
        return new PageImpl<>(List.of(
                new Hospede(4L,  "Adriana Teste", "1234567890257", "449999999856", "adriana@gmail.com", LocalDate.of(2013, 2, 21), LocalDateTime.of(2021, 10, 6, 1, 1, 1), Nacionalidade.BRASILEIRO, false, "Teste observacao Thiago"),
                new Hospede(5L, "Luan Teste", "1234567881257", "449999999487", "luan@gmail.com", LocalDate.of(2001, 1, 18), LocalDateTime.of(2015, 2, 7, 9, 1, 1), Nacionalidade.ESTRANGEIRO, false, "Teste observacao Thiago")
        ));
    }

    public static HospedeUpdateRequestDTO criarUpdateRequestDTO(String nome, String cpf, String telefone, String email, LocalDate dataNascimento, Nacionalidade nacionalidade, String observacao){
        return new HospedeUpdateRequestDTO(nome, cpf, telefone, email, dataNascimento, nacionalidade, observacao);
    }
}
