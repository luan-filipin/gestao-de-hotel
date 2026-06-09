package com.api.gestaodehotel.exceptions.quarto;

import java.util.List;

public class NumerosDeQuartosDuplicadosException extends RuntimeException {
    public NumerosDeQuartosDuplicadosException(List<Integer> numeroQuarto) {
        super("Números de quarto duplicados na requisição: " + numeroQuarto);
    }
}
