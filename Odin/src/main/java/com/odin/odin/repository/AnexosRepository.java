package com.odin.odin.repository;
import com.odin.odin.model.Anexos; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface AnexosRepository extends JpaRepository<Anexos,Long>{ List<Anexos> findByIdRadicadoOrderByIdAnexoDesc(Long idRadicado); }
