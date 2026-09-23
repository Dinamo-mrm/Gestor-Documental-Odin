package com.odin.odin.repository;

import com.odin.odin.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    Optional<Series> findByCodigoSerie(String codigoSerie);
}
