package com.odin.odin.repository;

import com.odin.odin.model.Permisos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PermisosRepository extends JpaRepository<Permisos, Long> {

    Optional<Permisos> findByNombreIgnoreCase(String nombre);

    @Query("SELECT p FROM Permisos p ORDER BY p.modulo ASC, p.id_permiso ASC")
    List<Permisos> findAllOrdenados();
}
