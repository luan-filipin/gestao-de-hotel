package com.api.gestaodehotel.domain;

import com.api.gestaodehotel.domain.enums.TipoQuarto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "quarto")
public class Quarto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer numeroQuarto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private TipoQuarto tipoQuarto;

    @Column(nullable = false)
    private Integer capacidade;

    @Column(nullable = false)
    private BigDecimal precoPorNoite;

    private String descricao;

    @Column(nullable = false)
    private Boolean ativo;
}
