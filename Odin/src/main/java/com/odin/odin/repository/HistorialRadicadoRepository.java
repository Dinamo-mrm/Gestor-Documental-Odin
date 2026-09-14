package com.odin.odin.repository;

import com.odin.odin.model.HistorialRadicado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistorialRadicadoRepository extends JpaRepository<HistorialRadicado, Long> {

    List<HistorialRadicado> findById_radicadoOrderByFechaDesc(Long idRadicado);

    List<HistorialRadicado> findById_usuarioOrderByFechaDesc(Long idUsuario);

    List<HistorialRadicado> findByAccionIgnoreCaseOrderByFechaDesc(String accion);

    List<HistorialRadicado> findTop100ByOrderByFechaDesc();
}
