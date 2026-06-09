package com.api.gestaodehotel.exceptions.hospede;

public class HospedeNaoExisteCpfException extends RuntimeException {
    public HospedeNaoExisteCpfException(String cpf) {
        super("Não existe um hospede com esse cpf: " + cpf);
    }
}
