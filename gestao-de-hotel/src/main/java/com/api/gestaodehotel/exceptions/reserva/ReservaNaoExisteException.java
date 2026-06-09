package com.api.gestaodehotel.exceptions.reserva;

public class ReservaNaoExisteException extends RuntimeException {
    public ReservaNaoExisteException(Long id) {
        super("A Reserva com id: " + id + " não foi encontrada");
    }
}
