package com.odin.odin.repository;

import com.odin.odin.model.HistorialRadicado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistorialRadicadoRepository extends JpaRepository<HistorialRadicado, Long> {
    List<HistorialRadicado> findById_radicadoOrderByFechaDesc(Long idRadicado);
}
