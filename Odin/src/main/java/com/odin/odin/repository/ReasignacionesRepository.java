package com.odin.odin.repository;

import com.odin.odin.model.Reasignaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReasignacionesRepository
        extends JpaRepository<Reasignaciones, Long> {

    @Query("""
        SELECT r
        FROM Reasignaciones r
        WHERE r.id_radicado = :idRadicado
        ORDER BY r.fecha DESC
    """)
    List<Reasignaciones> findByRadicadoOrderByFechaDesc(
            @Param("idRadicado") Integer idRadicado
    );
}