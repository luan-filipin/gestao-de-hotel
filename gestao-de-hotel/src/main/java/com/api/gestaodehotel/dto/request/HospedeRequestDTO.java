package com.api.gestaodehotel.dto.request;

import com.api.gestaodehotel.domain.enums.Nacionalidade;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record HospedeRequestDTO(

        @NotBlank(message = "O campo nome é obrigatorio")
        @Size(max = 150)
        String nome,

        @NotBlank(message = "O campo cpf é obrigatorio")
        @Size(max = 11)
        @CPF
        String cpf,

        @NotBlank(message = "O campo telefone é obrigatorio")
        @Size(max = 20)
        String telefone,

        @Size(max = 150)
        @Email(message = "informe um email valido")
        String email,

        @NotNull(message = "O campo data de nascimento é obrigatorio")
        @Past(message = "A data de nascimento deve ser anterior a data atual")
        LocalDate dataNascimento,

        @NotNull(message = "A nacionalidade é obrigatoria")
        @Size(max = 20)
        Nacionalidade nacionalidade,

        String descricao
) {
}
