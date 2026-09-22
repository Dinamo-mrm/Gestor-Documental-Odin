package com.odin.odin.repository;

import com.odin.odin.model.Observaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ObservacionesRepository
        extends JpaRepository<Observaciones, Long> {

    @Query("""
        SELECT o
        FROM Observaciones o
        WHERE o.id_radicado = :idRadicado
        ORDER BY o.fecha DESC
    """)
    List<Observaciones> findByRadicadoOrderByFechaDesc(
            @Param("idRadicado") Long idRadicado
    );
}