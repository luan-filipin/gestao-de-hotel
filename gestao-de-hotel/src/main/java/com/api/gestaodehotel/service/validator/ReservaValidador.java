package com.api.gestaodehotel.service.validator;

import com.api.gestaodehotel.domain.Reserva;
import com.api.gestaodehotel.exceptions.reserva.IdUrlDiferenteIdCorpoException;
import com.api.gestaodehotel.exceptions.reserva.ReservaExistenteException;
import com.api.gestaodehotel.exceptions.reserva.ReservaNaoExisteException;
import com.api.gestaodehotel.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReservaValidador {

    private final ReservaRepository reservaRepository;

    public void validaSeReservaJaExisteParaQuarto(Long id){
        if (reservaRepository.existsByQuartoId(id)) {
            throw new ReservaExistenteException(id);
        }
    }

    public Reserva validaSeReservaExiste(Long id){
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaNaoExisteException(id));
    }

    public void validaSeIdDoCorpoEIgualAoIdUrl(Long idUrl, Long idCorpo){
        if(!idUrl.equals(idCorpo)){
            throw new IdUrlDiferenteIdCorpoException(idUrl, idCorpo);
        }
    }
}
