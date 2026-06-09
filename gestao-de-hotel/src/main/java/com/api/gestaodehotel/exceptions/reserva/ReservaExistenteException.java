package com.api.gestaodehotel.exceptions.reserva;

public class ReservaExistenteException extends RuntimeException {
    public ReservaExistenteException(Long id) {
        super("Ja existe uma reserva para esse quarto: " + id);
    }
}
