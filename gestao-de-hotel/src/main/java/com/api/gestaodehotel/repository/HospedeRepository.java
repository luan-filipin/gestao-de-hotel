package com.api.gestaodehotel.repository;

import com.api.gestaodehotel.domain.Hospede;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HospedeRepository extends JpaRepository<Hospede, Long> {

    boolean existsByCpf(String cpf);
    Optional<Hospede> findByCpf(String cpf);

    @Query("SELECT h FROM Hospede h WHERE (:ativo IS NULL OR h.ativo = :ativo)")
    Page<Hospede> buscaTodosOsHospedesPorStatus(@Param("ativo") Boolean ativo, Pageable pageable);
}
