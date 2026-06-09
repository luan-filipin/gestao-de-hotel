package com.api.gestaodehotel.exceptions.hospede;

public class HospedeInativoException extends RuntimeException {
    public HospedeInativoException(Long id) {
        super("O Hospede com o id: " + id + " esta inativo");
    }
}
