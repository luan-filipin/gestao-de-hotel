package com.api.gestaodehotel.dto.request;

import com.api.gestaodehotel.domain.enums.Nacionalidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record HospedeRequestDTO(

        @NotBlank(message = "O campo nome é obrigatorio")
        String nome,

        @NotBlank(message = "O campo cpf é obrigatorio")
        String cpf,

        @NotBlank(message = "O campo telefone é obrigatorio")
        String telefone,

        String email,

        @NotNull(message = "O campo data de nascimento é obrigatorio")
        LocalDate dataNascimento,

        @NotNull(message = "A nacionalidade é obrigatoria")
        Nacionalidade nacionalidade,

        String descricao
) {
}
