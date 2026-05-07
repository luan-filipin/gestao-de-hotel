package com.api.gestaodehotel.exceptions;

public class QuartoExistenteException extends RuntimeException{
    public QuartoExistenteException(Integer numeroQuarto) {
        super("Ja existe um quarto com esse numero: " + numeroQuarto);
    }
}
