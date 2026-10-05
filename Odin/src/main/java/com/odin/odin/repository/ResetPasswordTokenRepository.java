package com.odin.odin.repository;

import com.odin.odin.model.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    Optional<ResetPasswordToken> findByToken(String token);
    void deleteByIdUsuario(Long idUsuario);
}
