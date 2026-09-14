package com.odin.odin.repository;

import com.odin.odin.model.Documentos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentosRepository extends JpaRepository<Documentos, Long> {

    List<Documentos> findByIdRadicado(Long idRadicado);
}
