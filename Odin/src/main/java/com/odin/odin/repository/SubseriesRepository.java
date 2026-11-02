package com.odin.odin.repository;

import com.odin.odin.model.Subseries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubseriesRepository extends JpaRepository<Subseries, Long> {

    @Query("SELECT s FROM Subseries s WHERE s.codigo_subserie = :codigoSubserie")
    Optional<Subseries> findByCodigoSubserie(@Param("codigoSubserie") String codigoSubserie);
}
