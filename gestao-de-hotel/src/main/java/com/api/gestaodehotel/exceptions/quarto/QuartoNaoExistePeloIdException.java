package com.api.gestaodehotel.exceptions.quarto;

public class QuartoNaoExistePeloIdException extends RuntimeException {
    public QuartoNaoExistePeloIdException(Long id) {
        super("O quarto com o ID: " + id + " não foi encontrado");
    }
}
