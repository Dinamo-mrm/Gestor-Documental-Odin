package com.odin.odin.repository;
import com.odin.odin.model.SesionesUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface SesionesUsuarioRepository extends JpaRepository<SesionesUsuario,Long> {
    Optional<SesionesUsuario> findFirstByToken(String token);
}
