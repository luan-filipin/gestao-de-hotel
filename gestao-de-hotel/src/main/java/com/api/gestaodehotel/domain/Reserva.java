package com.api.gestaodehotel.domain;

import com.api.gestaodehotel.domain.enums.StatusReserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_check_in", nullable = false)
    private LocalDateTime dataCheckIn;

    @Column(name = "quantidade_dias", nullable = false)
    private Integer quantidadeDias;

    @Column(name = "data_check_out")
    private LocalDateTime dataCheckOut;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private StatusReserva status = StatusReserva.PENDENTE;

    @ManyToOne
    @JoinColumn(name = "quarto_id", nullable = false)
    private Quarto quarto;

    @ManyToOne
    @JoinColumn(name = "hospede_id", nullable = false)
    private Hospede hospede;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(length = 300)
    private String observacao;


    public void calculaDataCheckOut(){
        this.dataCheckOut = this.dataCheckIn.plusDays(this.quantidadeDias);
    }

    public void calculaValorTotal(){
        this.valorTotal = this.quarto.getPrecoPorNoite().multiply(BigDecimal.valueOf(this.quantidadeDias));
    }
}
