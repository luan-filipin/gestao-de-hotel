package com.api.gestaodehotel.exceptions.quarto;

public class QuartoInativoException extends RuntimeException {
    public QuartoInativoException(Integer numeroQuarto) {
        super("O quarto: " + numeroQuarto + " esta inativo");
    }
}
