package com.odin.odin.repository;

import com.odin.odin.model.Subseries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubseriesRepository extends JpaRepository<Subseries, Long> {

    Optional<Subseries> findByCodigoSubserie(String codigoSubserie);
}
