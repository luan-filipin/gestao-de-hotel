package com.api.gestaodehotel.exceptions.hospede;

public class HospedeNaoExisteIdException extends RuntimeException {
    public HospedeNaoExisteIdException(Long id) {
        super("O hospede com o id: " + id + " não foi encontrado");
    }
}
