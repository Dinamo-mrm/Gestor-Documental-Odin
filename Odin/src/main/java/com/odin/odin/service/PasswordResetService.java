package com.odin.odin.service;

import com.odin.odin.model.ResetPasswordToken;
import com.odin.odin.model.Usuarios;
import com.odin.odin.repository.ResetPasswordTokenRepository;
import com.odin.odin.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {
    private final UsuariosRepository usuariosRepository;
    private final ResetPasswordTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final String baseUrl;
    private final String mailFrom;
    private final long expirationMinutes;

    public PasswordResetService(UsuariosRepository usuariosRepository,
                                ResetPasswordTokenRepository tokenRepository,
                                JavaMailSender mailSender,
                                PasswordEncoder passwordEncoder,
                                @Value("${app.reset-password.base-url:http://localhost:8080}") String baseUrl,
                                @Value("${app.mail.from:}") String mailFrom,
                                @Value("${app.reset-password.expiration-minutes:30}") long expirationMinutes) {
        this.usuariosRepository = usuariosRepository;
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.baseUrl = baseUrl;
        this.mailFrom = mailFrom;
        this.expirationMinutes = expirationMinutes;
    }

    @Transactional
    public Optional<RecuperacionResult> solicitarRecuperacion(String identificador) {
        if (identificador == null || identificador.isBlank()) {
            return Optional.empty();
        }

        String login = identificador.trim();
        Usuarios usuario = usuariosRepository.findByLoginFlexible(login).orElse(null);

        // Fallback extra por correo (por si el native query no mapea en algún entorno)
        if (usuario == null && login.contains("@")) {
            usuario = usuariosRepository.findByCorreoIgnoreCase(login).orElse(null);
        }

        if (usuario == null) {
            return Optional.empty();
        }

        String estado = usuario.getEstado() == null ? "" : usuario.getEstado().trim();
        if (!estado.equalsIgnoreCase("activo")) {
            return Optional.empty();
        }

        String correo = usuario.getCorreo() == null ? "" : usuario.getCorreo().trim();
        if (correo.isEmpty()) {
            return Optional.empty();
        }

        tokenRepository.deleteByIdUsuario(usuario.getId_usuario());
        String token = UUID.randomUUID().toString() + UUID.randomUUID().toString().replace("-", "");
        LocalDateTime ahora = LocalDateTime.now();
        ResetPasswordToken reset = ResetPasswordToken.builder()
                .idUsuario(usuario.getId_usuario())
                .token(token)
                .expiraEn(ahora.plusMinutes(expirationMinutes))
                .usado(false)
                .fechaCreacion(ahora)
                .build();
        tokenRepository.save(reset);

        String enlace = baseUrl.replaceAll("/$", "") + "/recuperar-password/restablecer?token=" + token;
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            if (mailFrom != null && !mailFrom.isBlank()) {
                mail.setFrom(mailFrom);
            }
            mail.setTo(correo);
            mail.setSubject("ODIN - Recuperación de contraseña");
            mail.setText("Hola " + (usuario.getNombre() == null ? "" : usuario.getNombre()) + ",\n\n"
                    + "Recibimos una solicitud para restablecer tu contraseña de ODIN.\n\n"
                    + "Ingresa al siguiente enlace:\n" + enlace + "\n\n"
                    + "El enlace vence en " + expirationMinutes + " minutos y solo puede utilizarse una vez.\n\n"
                    + "Si no solicitaste este cambio, puedes ignorar este mensaje.\n\n"
                    + "ODIN - Sistema de Gestión Documental");
            mailSender.send(mail);
        } catch (Exception ignored) {
            // Token válido aunque falle el correo (SMTP no configurado).
        }

        return Optional.of(new RecuperacionResult(token, correo, usuario.getNombre()));
    }

    @Transactional(readOnly = true)
    public boolean tokenValido(String token) {
        return tokenRepository.findByToken(token)
                .filter(t -> Boolean.FALSE.equals(t.getUsado()))
                .filter(t -> t.getExpiraEn() != null && t.getExpiraEn().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    @Transactional
    public boolean restablecer(String token, String nuevaPassword) {
        if (nuevaPassword == null || nuevaPassword.length() < 8) return false;
        ResetPasswordToken reset = tokenRepository.findByToken(token).orElse(null);
        if (reset == null || Boolean.TRUE.equals(reset.getUsado()) || reset.getExpiraEn() == null
                || !reset.getExpiraEn().isAfter(LocalDateTime.now())) {
            return false;
        }

        Usuarios usuario = usuariosRepository.findById(reset.getIdUsuario()).orElse(null);
        if (usuario == null) return false;
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuariosRepository.save(usuario);
        reset.setUsado(true);
        tokenRepository.save(reset);
        return true;
    }

    public static final class RecuperacionResult {
        private final String token;
        private final String correoEnmascarado;
        private final String nombre;

        public RecuperacionResult(String token, String correo, String nombre) {
            this.token = token;
            this.correoEnmascarado = enmascararCorreo(correo);
            this.nombre = nombre;
        }

        public String getToken() { return token; }
        public String getCorreoEnmascarado() { return correoEnmascarado; }
        public String getNombre() { return nombre; }

        private static String enmascararCorreo(String correo) {
            if (correo == null || !correo.contains("@")) return "***";
            String[] p = correo.split("@", 2);
            String local = p[0];
            String mask = local.length() <= 2
                    ? "*".repeat(Math.max(local.length(), 1))
                    : local.charAt(0) + "*".repeat(local.length() - 2) + local.charAt(local.length() - 1);
            return mask + "@" + p[1];
        }
    }
}
