package com.odin.odin.repository;

import com.odin.odin.model.Expedientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExpedientesRepository extends JpaRepository<Expedientes, Long> {

    @Query("SELECT e FROM Expedientes e WHERE e.codigo_expediente = :codigo")
    Optional<Expedientes> findByCodigo_expediente(@Param("codigo") String codigo);
}
