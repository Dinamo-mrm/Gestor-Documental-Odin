package com.odin.odin.repository;

import com.odin.odin.model.Notificaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificacionesRepository extends JpaRepository<Notificaciones, Long> {

    @Query("SELECT n FROM Notificaciones n WHERE n.id_usuario = :idUsuario ORDER BY n.fecha DESC")
    List<Notificaciones> findById_usuarioOrderByFechaDesc(@Param("idUsuario") Long idUsuario);

    @Query(value = "SELECT * FROM notificaciones WHERE id_usuario = :idUsuario ORDER BY fecha DESC LIMIT 10", nativeQuery = true)
    List<Notificaciones> findTop10ById_usuarioOrderByFechaDesc(@Param("idUsuario") Long idUsuario);

    @Query("SELECT COUNT(n) FROM Notificaciones n WHERE n.id_usuario = :idUsuario AND (n.leida = false OR n.leida IS NULL)")
    long countById_usuarioAndLeidaFalse(@Param("idUsuario") Long idUsuario);
}
