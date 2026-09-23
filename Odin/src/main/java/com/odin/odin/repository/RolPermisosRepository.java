package com.odin.odin.repository;

import com.odin.odin.model.Permisos;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RolPermisosRepository extends Repository<Permisos, Long> {

    @Query(value = """
            SELECT p.*
            FROM permisos p
            INNER JOIN rol_permisos rp ON rp.id_permiso = p.id_permiso
            WHERE rp.id_rol = :idRol
            ORDER BY p.id_permiso
            """, nativeQuery = true)
    List<Permisos> buscarPermisosPorRol(@Param("idRol") Long idRol);

    @Query(value = """
            SELECT rp.id_permiso
            FROM rol_permisos rp
            WHERE rp.id_rol = :idRol
            ORDER BY rp.id_permiso
            """, nativeQuery = true)
    List<Long> buscarIdsPermisosPorRol(@Param("idRol") Long idRol);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM rol_permisos WHERE id_rol = :idRol", nativeQuery = true)
    void eliminarPermisosPorRol(@Param("idRol") Long idRol);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO rol_permisos (id_rol, id_permiso)
            VALUES (:idRol, :idPermiso)
            """, nativeQuery = true)
    void asignarPermiso(@Param("idRol") Long idRol,
                        @Param("idPermiso") Long idPermiso);
}
