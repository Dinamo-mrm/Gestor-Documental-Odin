package com.odin.odin.repository;

import com.odin.odin.model.Observaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ObservacionesRepository extends JpaRepository<Observaciones, Long> {
    List<Observaciones> findById_radicadoOrderByFechaDesc(Long idRadicado);
}
