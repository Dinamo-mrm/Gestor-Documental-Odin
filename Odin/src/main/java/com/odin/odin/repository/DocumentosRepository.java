package com.odin.odin.repository;

import com.odin.odin.model.Documentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentosRepository extends JpaRepository<Documentos, Long> {

    @Query("SELECT d FROM Documentos d WHERE d.id_radicado = :radicado ORDER BY d.id_documento DESC")
    List<Documentos> buscarPorRadicado(@Param("radicado") Long radicado);
}
