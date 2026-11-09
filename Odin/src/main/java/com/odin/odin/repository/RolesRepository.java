package com.odin.odin.repository;

import com.odin.odin.model.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RolesRepository
        extends JpaRepository<Roles, Long> {

    Optional<Roles> findByRolIgnoreCase(String rol);

    Optional<Roles> findByNombreIgnoreCase(String nombre);

    @Query(value = """
            SELECT * FROM roles r
            WHERE (:buscar IS NULL OR :buscar = ''
                   OR LOWER(r.nombre) LIKE LOWER(CONCAT('%', :buscar, '%'))
                   OR LOWER(r.rol) LIKE LOWER(CONCAT('%', :buscar, '%')))
              AND (:estado IS NULL OR :estado = '' OR LOWER(r.estado) = LOWER(:estado))
            ORDER BY r.id_rol ASC
            """,
            countQuery = """
            SELECT count(*) FROM roles r
            WHERE (:buscar IS NULL OR :buscar = ''
                   OR LOWER(r.nombre) LIKE LOWER(CONCAT('%', :buscar, '%'))
                   OR LOWER(r.rol) LIKE LOWER(CONCAT('%', :buscar, '%')))
              AND (:estado IS NULL OR :estado = '' OR LOWER(r.estado) = LOWER(:estado))
            """,
            nativeQuery = true)
    Page<Roles> buscarConFiltros(@Param("buscar") String buscar,
                                @Param("estado") String estado,
                                Pageable pageable);
}