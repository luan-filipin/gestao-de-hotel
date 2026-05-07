package com.api.gestaodehotel.exceptions;

public class QuartoNaoExisteException extends RuntimeException{
    public QuartoNaoExisteException(Integer numeroQuarto) {
        super("O quarto " + numeroQuarto + " não foi encontrado");
    }
}
