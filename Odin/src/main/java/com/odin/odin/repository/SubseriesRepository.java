package com.odin.odin.repository;

import com.odin.odin.model.Series;
import com.odin.odin.model.Subseries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubseriesRepository extends JpaRepository<Subseries, Long> {

    Optional<Subseries> findByCodigoSubserie(String codigoSubserie);

    @Query("select s.serie from Subseries s where s.codigo_subserie = :codigo")
    Optional<Series> findSerieByCodigoSubserie(@Param("codigo") String codigo);

    @Query("select s from Series s where s.codigo_serie = :codigo")
    Optional<Series> findSerieByCodigo(@Param("codigo") String codigo);
}
