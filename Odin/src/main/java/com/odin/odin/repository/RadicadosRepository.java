package com.odin.odin.repository;

import com.odin.odin.model.Radicados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RadicadosRepository extends JpaRepository<Radicados, Long> {

    @Query("SELECT r FROM Radicados r ORDER BY r.id_radicado DESC")
    List<Radicados> findTop5UltimosRadicados();

    @Query("SELECT COUNT(r) FROM Radicados r WHERE r.id_estado = 1")
    Long countPendientes();

    @Query("SELECT COUNT(r) FROM Radicados r WHERE r.id_estado = 2")
    Long countEnTramite();

    @Query("SELECT COUNT(r) FROM Radicados r WHERE r.id_estado = 3")
    Long countFinalizados();

    @Query("SELECT COUNT(r) FROM Radicados r WHERE r.id_estado = 4")
    Long countRechazados();

    @Query("SELECT COUNT(r) FROM Radicados r WHERE r.fecha_vencimiento IS NOT NULL AND r.fecha_vencimiento < :fecha")
    Long countVencidos(@Param("fecha") String fecha);

    @Query("SELECT COUNT(r) FROM Radicados r WHERE r.id_estado = :estadoId")
    Long countByEstado(@Param("estadoId") Long estadoId);

    @Query("SELECT r FROM Radicados r " +
           "WHERE (:texto IS NULL OR :texto = '' OR " +
           "LOWER(r.numero_radicado) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(r.asunto) LIKE LOWER(CONCAT('%', :texto, '%'))) " +
           "AND (:estado IS NULL OR r.id_estado = :estado) " +
           "AND (:dependencia IS NULL OR r.id_dependencia = :dependencia) " +
           "AND (:tramite IS NULL OR r.id_tramite = :tramite) " +
           "ORDER BY r.id_radicado DESC")
    List<Radicados> buscar(@Param("texto") String texto,
                           @Param("estado") Integer estado,
                           @Param("dependencia") Long dependencia,
                           @Param("tramite") Integer tramite);
}
