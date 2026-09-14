package com.odin.odin.repository;

import com.odin.odin.model.Reasignaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReasignacionesRepository extends JpaRepository<Reasignaciones, Long> {
    List<Reasignaciones> findById_radicadoOrderByFechaDesc(Integer idRadicado);
}
