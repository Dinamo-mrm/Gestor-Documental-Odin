package com.odin.odin.repository;

import com.odin.odin.model.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {
    Optional<PasswordResetCode> findTopByIdUsuarioAndCodigoAndUsadoFalseOrderByFechaCreacionDesc(Long idUsuario, String codigo);
    List<PasswordResetCode> findByIdUsuarioAndUsadoFalse(Long idUsuario);
}
