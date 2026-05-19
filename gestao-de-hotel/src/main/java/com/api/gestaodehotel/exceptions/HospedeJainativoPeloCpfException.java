package com.api.gestaodehotel.exceptions;

public class HospedeJainativoPeloCpfException extends RuntimeException {
    public HospedeJainativoPeloCpfException(String cpf) {
        super("O hospede com cpf: " + cpf + " ja esta inativo");
    }
}
