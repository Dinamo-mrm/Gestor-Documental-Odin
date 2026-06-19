package com.odin.odin.repository;

import com.odin.odin.model.Radicados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RadicadosRepository extends JpaRepository<Radicados, Long> {

    // ✅ Usar @Query explícito
    @Query("SELECT r FROM Radicados r ORDER BY r.id_radicado DESC LIMIT 5")
    List<Radicados> findTop5UltimosRadicados();

    // Métodos para dashboard
    @Query("SELECT COUNT(r) FROM Radicados r WHERE r.id_estado = 1")
    Long countPendientes();

    @Query("SELECT COUNT(r) FROM Radicados r WHERE r.id_estado = 3")
    Long countFinalizados();
}