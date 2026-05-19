package com.api.gestaodehotel.exceptions;

public class HospedeJaExisteException extends RuntimeException {
    public HospedeJaExisteException(String cpf) {
        super("Ja existe um hospede com esse cpf: " + cpf);
    }
}
