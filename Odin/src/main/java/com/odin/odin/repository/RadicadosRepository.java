package com.odin.odin.repository;

import com.odin.odin.model.Radicados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
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

    @Query(value = "SELECT COUNT(*) FROM radicados " +
            "WHERE NULLIF(TRIM(fecha_vencimiento), '') IS NOT NULL " +
            "AND NULLIF(TRIM(fecha_vencimiento), '')::date < CURRENT_DATE " +
            "AND id_estado NOT IN (3,4)", nativeQuery = true)
    Long countVencidos();

    @Query(value = "SELECT * FROM radicados " +
            "WHERE NULLIF(TRIM(fecha_vencimiento), '') IS NOT NULL " +
            "AND NULLIF(TRIM(fecha_vencimiento), '')::date < CURRENT_DATE " +
            "AND id_estado NOT IN (3,4) " +
            "ORDER BY NULLIF(TRIM(fecha_vencimiento), '')::date ASC", nativeQuery = true)
    List<Radicados> findVencidos();

    @Query(value = "SELECT * FROM radicados " +
            "WHERE NULLIF(TRIM(fecha_vencimiento), '') IS NOT NULL " +
            "AND NULLIF(TRIM(fecha_vencimiento), '')::date BETWEEN CURRENT_DATE AND (CURRENT_DATE + CAST(:dias AS integer)) " +
            "AND id_estado NOT IN (3,4) " +
            "ORDER BY NULLIF(TRIM(fecha_vencimiento), '')::date ASC", nativeQuery = true)
    List<Radicados> findProximosAVencer(@Param("dias") Integer dias);

    @Query(value = "SELECT COUNT(*) FROM radicados " +
            "WHERE id_usuario IS NULL OR id_usuario = 0", nativeQuery = true)
    Long countSinAsignar();

    @Query(value = "SELECT COUNT(*) FROM radicados " +
            "WHERE id_estado = 3 " +
            "AND fecha_cierre IS NOT NULL " +
            "AND DATE(fecha_cierre) = CURRENT_DATE", nativeQuery = true)
    Long countFinalizadosHoy();

    @Query(value = "SELECT COUNT(*) FROM radicados " +
            "WHERE NULLIF(TRIM(fecha_vencimiento), '') IS NOT NULL " +
            "AND NULLIF(TRIM(fecha_vencimiento), '')::date BETWEEN CURRENT_DATE AND (CURRENT_DATE + 7) " +
            "AND id_estado NOT IN (3,4)", nativeQuery = true)
    Long countProximosAVencer();

    @Query("SELECT COUNT(r) FROM Radicados r WHERE r.id_estado = :estadoId")
    Long countByEstado(@Param("estadoId") Long estadoId);

    @Query("SELECT r FROM Radicados r WHERE (:texto IS NULL OR :texto = '' OR LOWER(r.numero_radicado) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(r.asunto) LIKE LOWER(CONCAT('%', :texto, '%'))) AND (:estado IS NULL OR r.id_estado = :estado) AND (:dependencia IS NULL OR r.id_dependencia = :dependencia) AND (:tramite IS NULL OR r.id_tramite = :tramite) ORDER BY r.id_radicado DESC")
    List<Radicados> buscar(@Param("texto") String texto, @Param("estado") Integer estado, @Param("dependencia") Long dependencia, @Param("tramite") Integer tramite);

    @Modifying
    @Transactional
    @Query(value = "UPDATE radicados SET id_usuario=:usuario, id_dependencia=:dependencia WHERE id_radicado=:radicado", nativeQuery = true)
    int actualizarAsignacion(@Param("radicado") Long radicado, @Param("usuario") Integer usuario, @Param("dependencia") Integer dependencia);
}
