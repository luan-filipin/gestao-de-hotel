package com.api.gestaodehotel.service.validator;

import com.api.gestaodehotel.domain.Quarto;
import com.api.gestaodehotel.exceptions.QuartoEstaInativoException;
import com.api.gestaodehotel.exceptions.QuartoExistenteException;
import com.api.gestaodehotel.exceptions.QuartoNaoExisteException;
import com.api.gestaodehotel.repository.QuartoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class QuartoValidador {

    private final QuartoRepository quartoRepository;

    public void validarQuartoExistente(Integer numeroQuarto) {
        if (quartoRepository.existsByNumeroQuarto(numeroQuarto)) {
            throw new QuartoExistenteException(numeroQuarto);
        }
    }

    public Quarto buscaQuartoOuLancarException(Integer numeroQuarto) {
        return quartoRepository.findByNumeroQuarto(numeroQuarto)
                .orElseThrow(() -> new QuartoNaoExisteException(numeroQuarto));
    }

    public Quarto buscaQuartoEValidaSeInativoOuLancaException(Integer numeroQuarto) {
        Quarto quarto =  quartoRepository.findByNumeroQuarto(numeroQuarto)
                .orElseThrow(() -> new QuartoNaoExisteException(numeroQuarto));

        if (Boolean.FALSE.equals(quarto.getAtivo())) {
            throw new QuartoEstaInativoException(numeroQuarto);
        }
        return quarto;
    }
}
