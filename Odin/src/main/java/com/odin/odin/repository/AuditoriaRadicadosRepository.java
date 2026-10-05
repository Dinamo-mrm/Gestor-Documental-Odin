package com.odin.odin.repository;

import com.odin.odin.model.AuditoriaRadicados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuditoriaRadicadosRepository extends JpaRepository<AuditoriaRadicados, Long> {

    @Query("SELECT a FROM AuditoriaRadicados a WHERE a.id_radicado = :idRadicado ORDER BY a.fecha DESC")
    List<AuditoriaRadicados> findById_radicadoOrderByFechaDesc(@Param("idRadicado") Long idRadicado);
}
