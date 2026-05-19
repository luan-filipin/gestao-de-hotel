package com.api.gestaodehotel.service.validator;

import com.api.gestaodehotel.domain.Hospede;
import com.api.gestaodehotel.exceptions.HospedeJaExisteException;
import com.api.gestaodehotel.exceptions.HospedeJainativoPeloCpfException;
import com.api.gestaodehotel.exceptions.HospedeNaoExisteCpfException;
import com.api.gestaodehotel.repository.HospedeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HospedeValidador {

    private final HospedeRepository hospedeRepository;

    public void validaSeHospedeExiste(String cpf){
        if (hospedeRepository.existsByCpf(cpf)) {
            throw new HospedeJaExisteException(cpf);
        }
    }

    public Hospede validaSeHospedeNaoExistePeloCpf(String cpf){
        return hospedeRepository.findByCpf(cpf)
                .orElseThrow(() -> new HospedeNaoExisteCpfException(cpf));
    }

    public Hospede validaSeHospedeExisteEInativoPeloCpf(String cpf){
        Hospede hospedeExistente = validaSeHospedeNaoExistePeloCpf(cpf);

        if (Boolean.FALSE.equals(hospedeExistente.getAtivo())) {
            throw new HospedeJainativoPeloCpfException(cpf);
        }

        return hospedeExistente;
    }

}
