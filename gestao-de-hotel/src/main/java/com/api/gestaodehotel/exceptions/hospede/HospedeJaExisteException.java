package com.api.gestaodehotel.exceptions.hospede;

public class HospedeJaExisteException extends RuntimeException {
    public HospedeJaExisteException(String cpf) {
        super("Ja existe um hospede com esse cpf: " + cpf);
    }
}
