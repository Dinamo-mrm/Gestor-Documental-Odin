package com.odin.odin.repository;

import com.odin.odin.model.Radicados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RadicadosRepository extends JpaRepository<Radicados, Long> {

    // Últimos 5 radicados
    @Query("SELECT r FROM Radicados r ORDER BY r.id_radicado DESC")
    List<Radicados> findTop5UltimosRadicados();

    // Estadísticas para KPIs
    @Query("SELECT COUNT(r) FROM Radicados r WHERE r.id_estado = 1")
    Long countPendientes();

    @Query("SELECT COUNT(r) FROM Radicados r WHERE r.id_estado = 2")
    Long countEnTramite();

    @Query("SELECT COUNT(r) FROM Radicados r WHERE r.id_estado = 3")
    Long countFinalizados();

    @Query("SELECT COUNT(r) FROM Radicados r WHERE r.id_estado = 4")
    Long countRechazados();

    // ⚠️ Solo si existe el campo fecha_vencimiento
    // @Query("SELECT COUNT(r) FROM Radicados r WHERE r.fecha_vencimiento < CURRENT_DATE")
    // Long countVencidos();

    // Si NO existe fecha_vencimiento, usa este método alternativo
    default Long countVencidos() {
        return 0L;  // O implementar con otra lógica
    }

    // Contar por estado específico
    @Query("SELECT COUNT(r) FROM Radicados r WHERE r.id_estado = :estadoId")
    Long countByEstado(@Param("estadoId") Long estadoId);
}