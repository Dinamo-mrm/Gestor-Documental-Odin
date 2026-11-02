package com.odin.odin.repository;

import com.odin.odin.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    @Query("SELECT s FROM Series s WHERE s.codigo_serie = :codigoSerie")
    Optional<Series> findByCodigoSerie(@Param("codigoSerie") String codigoSerie);
}
