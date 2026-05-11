package com.odin.odin.repository;

import com.odin.odin.model.Documentos;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentosRepository extends JpaRepository<Documentos, Long>
{

}
