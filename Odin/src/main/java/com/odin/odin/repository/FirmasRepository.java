package com.odin.odin.repository;
import com.odin.odin.model.Firmas; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface FirmasRepository extends JpaRepository<Firmas,Long>{ List<Firmas> findByIdRadicadoOrderByFechaFirmaDesc(Long idRadicado); }
