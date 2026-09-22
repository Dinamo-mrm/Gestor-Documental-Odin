package com.odin.odin.repository;

import com.odin.odin.model.HistorialRadicado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistorialRadicadoRepository
        extends JpaRepository<HistorialRadicado, Long> {

    @Query("""
        SELECT h
        FROM HistorialRadicado h
        WHERE h.id_radicado = :idRadicado
        ORDER BY h.fecha DESC
    """)
    List<HistorialRadicado> findByRadicadoOrderByFechaDesc(
            @Param("idRadicado") Long idRadicado
    );

    @Query("""
        SELECT h
        FROM HistorialRadicado h
        WHERE h.id_usuario = :idUsuario
        ORDER BY h.fecha DESC
    """)
    List<HistorialRadicado> findByUsuarioOrderByFechaDesc(
            @Param("idUsuario") Long idUsuario
    );

    List<HistorialRadicado> findByAccionIgnoreCaseOrderByFechaDesc(
            String accion
    );

    List<HistorialRadicado> findTop100ByOrderByFechaDesc();
}