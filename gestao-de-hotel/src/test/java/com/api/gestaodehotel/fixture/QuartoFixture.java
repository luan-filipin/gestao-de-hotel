package com.api.gestaodehotel.fixture;

import com.api.gestaodehotel.domain.Quarto;
import com.api.gestaodehotel.domain.enums.TipoQuarto;
import com.api.gestaodehotel.dto.request.QuartoRequestDTO;
import com.api.gestaodehotel.dto.request.QuartoUpdateRequestDTO;

import java.math.BigDecimal;
import java.util.List;

public class QuartoFixture {

    public static QuartoRequestDTO criarRequestDTO(Integer numeroQuarto){
        return new QuartoRequestDTO(
                numeroQuarto,
                TipoQuarto.SOLTEIRO,
                1,
                new BigDecimal("175.00"),
                "Teste descricao");
    }

    public static Quarto criarQuarto(
            Long id,
            Integer numeroQuarto,
            TipoQuarto tipoQuarto,
            Integer capacidade,
            BigDecimal precoPorNoite,
            Boolean ativo){
        return new Quarto(
                id,
                numeroQuarto,
                tipoQuarto,
                capacidade,
                precoPorNoite,
                "Teste descricao",
                ativo

        );
    }

    public static List<Quarto> criarListaDeQuartosTrue(){
        Quarto quarto = criarQuarto(1L, 1, TipoQuarto.SOLTEIRO, 1, new BigDecimal("175.00"),true);
        Quarto quarto2 = criarQuarto(2L, 2, TipoQuarto.CASAL, 2, new BigDecimal("200.00"), true);
        Quarto quarto3 = criarQuarto(3L, 3, TipoQuarto.SOLTEIRO, 3, new BigDecimal("150.00"), true);

        return List.of(quarto, quarto2, quarto3);
    }

    public static List<Quarto> criarListaDeQuartosFalse(){
        Quarto quarto = criarQuarto(4L, 1, TipoQuarto.SOLTEIRO, 1, new BigDecimal("175.00"), false);
        Quarto quarto2 = criarQuarto(5L, 2, TipoQuarto.CASAL, 2, new BigDecimal("200.00"), false);
        Quarto quarto3 = criarQuarto(6L, 3, TipoQuarto.SOLTEIRO, 3, new BigDecimal("150.00"), false);

        return List.of(quarto, quarto2, quarto3);
    }

    public static QuartoUpdateRequestDTO criarUpdateRequestDTO(TipoQuarto tipoQuarto, Integer capacidade, BigDecimal precoPorNoite, String descricao){
        return new QuartoUpdateRequestDTO(
                tipoQuarto,
                capacidade,
                precoPorNoite,
                descricao
        );
    }

    public static List<QuartoRequestDTO> criarListaDeQuartosEmLoteDuplicados(){
        return List.of(
                new QuartoRequestDTO(1, TipoQuarto.SOLTEIRO, 1, new BigDecimal("130.00"), "Teste descricao solteiro"),
                new QuartoRequestDTO(1, TipoQuarto.CASAL, 2, new BigDecimal("250.00"), "Teste descricao casal"),
                new QuartoRequestDTO(2, TipoQuarto.FAMILIA, 3, new BigDecimal("380.00"), "Teste descricao familia"),
                new QuartoRequestDTO(2, TipoQuarto.SUITE, 2, new BigDecimal("480.00"), "Teste descricao suite")
        );
    }
}
