package com.api.gestaodehotel.repository;

import com.api.gestaodehotel.domain.Quarto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuartoRepository extends JpaRepository<Quarto, Long> {

    boolean existsByNumeroQuarto(Integer numeroQuarto);
    Optional<Quarto> findByNumeroQuarto(Integer numeroQuarto);

    @Query("SELECT q FROM Quarto q WHERE (:ativo IS NULL OR q.ativo = :ativo)")
    Page<Quarto> findByAtivo(@Param("ativo") Boolean ativo, Pageable pageable);
}
