package com.api.gestaodehotel.exceptions.reserva;

public class IdUrlDiferenteIdCorpoException extends RuntimeException {
    public IdUrlDiferenteIdCorpoException(Long idUrl, Long idCorpo) {
        super("O id da url: " + idUrl + " é diferente do id do corpo: " + idCorpo);
    }
}
