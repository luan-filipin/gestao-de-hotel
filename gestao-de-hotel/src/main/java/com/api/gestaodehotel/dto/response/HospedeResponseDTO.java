package com.api.gestaodehotel.dto.response;

import com.api.gestaodehotel.domain.enums.Nacionalidade;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record HospedeResponseDTO (

        Long id,
        String codigo,
        String cpf,
        String telefone,
        String email,
        LocalDate dataNascimento,
        LocalDateTime dataCadastro,
        Nacionalidade nacionalidade,
        Boolean ativo,
        String descricao
){
}
