package com.api.gestaodehotel.repository;

import com.api.gestaodehotel.domain.Hospede;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospedeRepository extends JpaRepository<Hospede, Long> {
}
