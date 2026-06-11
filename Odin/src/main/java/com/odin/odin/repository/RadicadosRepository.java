package com.odin.odin.repository;

import com.odin.odin.model.Radicados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RadicadosRepository extends JpaRepository<Radicados, Long> {

    @Query("""
        SELECT COUNT(r)
        FROM Radicados r
        WHERE r.idEstado IN (1,2)
    """)
    Long countPendientes();

    @Query("""
        SELECT COUNT(r)
        FROM Radicados r
        WHERE r.idEstado IN (5,7)
    """)
    Long countFinalizados();

    @Query("""
        SELECT COUNT(r)
        FROM Radicados r
        WHERE r.idEstado IN (3,4)
    """)
    Long countProblemas();

    @Query("""
        SELECT COUNT(r)
        FROM Radicados r
        WHERE r.fechaVencimiento IS NOT NULL
          AND r.fechaVencimiento < CURRENT_TIMESTAMP
          AND r.idEstado NOT IN (5,7)
    """)
    Long countVencidos();

    List<Radicados> findTop10ByOrderByIdRadicadoDesc();
}