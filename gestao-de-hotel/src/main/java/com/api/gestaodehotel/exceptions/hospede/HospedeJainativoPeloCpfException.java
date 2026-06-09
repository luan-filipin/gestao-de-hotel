package com.api.gestaodehotel.exceptions.hospede;

public class HospedeJainativoPeloCpfException extends RuntimeException {
    public HospedeJainativoPeloCpfException(String cpf) {
        super("O hospede com cpf: " + cpf + " ja esta inativo");
    }
}
