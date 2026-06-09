package com.api.gestaodehotel.repository;

import com.api.gestaodehotel.domain.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByQuartoId(Long id);

    @Query("SELECT q FROM Reserva q WHERE (:ativo IS NULL OR q.ativo = :ativo)")
    Page<Reserva> findByAtivo(@Param("ativo") Boolean ativo, Pageable pageable);
}
