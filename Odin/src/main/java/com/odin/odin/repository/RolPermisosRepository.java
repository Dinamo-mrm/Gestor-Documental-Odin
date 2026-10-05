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

    @Query(value = "SELECT * FROM permisos ORDER BY modulo, accion, nombre", nativeQuery = true)
    List<Permisos> todosLosPermisos();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM rol_permisos WHERE id_rol = :idRol", nativeQuery = true)
    int eliminarPermisosDelRol(@Param("idRol") Long idRol);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO rol_permisos (id_rol, id_permiso)
            SELECT :idRol, :idPermiso
            WHERE EXISTS (SELECT 1 FROM roles WHERE id_rol = :idRol)
              AND EXISTS (SELECT 1 FROM permisos WHERE id_permiso = :idPermiso)
              AND NOT EXISTS (
                    SELECT 1 FROM rol_permisos
                    WHERE id_rol = :idRol AND id_permiso = :idPermiso
              )
            """, nativeQuery = true)
    int asignarPermiso(@Param("idRol") Long idRol, @Param("idPermiso") Long idPermiso);
}
