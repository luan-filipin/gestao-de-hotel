package com.api.gestaodehotel.exceptions;

public class HospedeNaoExisteCpfException extends RuntimeException {
    public HospedeNaoExisteCpfException(String cpf) {
        super("Não existe um hospede com esse cpf: " + cpf);
    }
}
