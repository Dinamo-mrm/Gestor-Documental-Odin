package com.odin.odin.repository;

import com.odin.odin.model.Tramites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TramitesRepository extends JpaRepository<Tramites, Long> {

    List<Tramites> findByIdDependenciaResponsable(Long idDependencia);

    List<Tramites> findByActivoTrue();

    List<Tramites> findByNombreContainingIgnoreCase(String nombre);

    List<Tramites> findByRequiereRespuestaTrue();
}