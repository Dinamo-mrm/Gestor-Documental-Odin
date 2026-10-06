package com.odin.odin.service;

import com.odin.odin.model.PasswordResetCode;
import com.odin.odin.model.Usuarios;
import com.odin.odin.repository.PasswordResetCodeRepository;
import com.odin.odin.repository.UsuariosRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordRecoveryService {
    private final UsuariosRepository usuariosRepository;
    private final PasswordResetCodeRepository resetCodeRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom random = new SecureRandom();
    public PasswordRecoveryService(UsuariosRepository usuariosRepository, PasswordResetCodeRepository resetCodeRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.usuariosRepository = usuariosRepository; this.resetCodeRepository = resetCodeRepository; this.emailService = emailService; this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void solicitarRecuperacion(String correo) {
        if (correo == null || correo.isBlank()) return;
        Optional<Usuarios> opt = usuariosRepository.findByCorreoIgnoreCase(correo.trim());
        if (opt.isEmpty()) return;
        Usuarios usuario = opt.get();
        resetCodeRepository.findByIdUsuarioAndUsadoFalse(usuario.getId_usuario()).forEach(c -> { c.setUsado(true); resetCodeRepository.save(c); });
        String codigo = String.format("%06d", random.nextInt(1_000_000));
        LocalDateTime ahora = LocalDateTime.now();
        PasswordResetCode resetCode = new PasswordResetCode();
        resetCode.setIdUsuario(usuario.getId_usuario());
        resetCode.setCodigo(codigo);
        resetCode.setFechaCreacion(ahora);
        resetCode.setFechaExpiracion(ahora.plusMinutes(10));
        resetCode.setUsado(false);
        resetCodeRepository.save(resetCode);
        emailService.enviarCodigoRecuperacion(usuario.getCorreo(), usuario.getNombre(), codigo);
    }
    public boolean validarCodigo(String correo, String codigo) {
        if (correo == null || correo.isBlank() || codigo == null || codigo.isBlank()) return false;
        Optional<Usuarios> u = usuariosRepository.findByCorreoIgnoreCase(correo.trim());
        if (u.isEmpty()) return false;
        Optional<PasswordResetCode> c = resetCodeRepository.findTopByIdUsuarioAndCodigoAndUsadoFalseOrderByFechaCreacionDesc(u.get().getId_usuario(), codigo.trim());
        return c.isPresent() && !c.get().estaUsado() && !c.get().estaExpirado();
    }
    @Transactional
    public boolean cambiarPassword(String correo, String codigo, String nuevaPassword) {
        if (nuevaPassword == null || nuevaPassword.length() < 8 || !validarCodigo(correo, codigo)) return false;
        Optional<Usuarios> u = usuariosRepository.findByCorreoIgnoreCase(correo.trim());
        if (u.isEmpty()) return false;
        Optional<PasswordResetCode> c = resetCodeRepository.findTopByIdUsuarioAndCodigoAndUsadoFalseOrderByFechaCreacionDesc(u.get().getId_usuario(), codigo.trim());
        if (c.isEmpty() || c.get().estaExpirado()) return false;
        Usuarios usuario = u.get();
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuariosRepository.save(usuario);
        c.get().setUsado(true); resetCodeRepository.save(c.get());
        return true;
    }
}
