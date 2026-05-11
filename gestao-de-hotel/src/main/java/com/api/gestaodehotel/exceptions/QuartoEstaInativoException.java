package com.api.gestaodehotel.exceptions;

public class QuartoEstaInativoException extends RuntimeException {
    public QuartoEstaInativoException(Integer numeroQuarto) {
        super("O quarto " + numeroQuarto + " ja esta inativo");
    }
}
