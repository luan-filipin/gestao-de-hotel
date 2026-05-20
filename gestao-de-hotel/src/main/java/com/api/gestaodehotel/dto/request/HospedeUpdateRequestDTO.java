package com.api.gestaodehotel.dto.request;

import com.api.gestaodehotel.domain.enums.Nacionalidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record HospedeUpdateRequestDTO(
        @Size(max = 150)
        String nome,

        @NotBlank(message = "O campo CPF é obrigatorio")
        @Size(max = 11)
        String cpf,

        @Size(max = 20)
        String telefone,

        @Size(max = 150)
        String email,

        @Past(message = "A data de nascimento deve ser anterior a data atual")
        LocalDate dataNascimento,
        Nacionalidade nacionalidade,

        @Size(max = 300)
        String observacao
) {

}
