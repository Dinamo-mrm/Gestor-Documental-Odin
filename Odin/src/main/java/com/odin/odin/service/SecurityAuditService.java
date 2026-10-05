package com.odin.odin.service;

import com.odin.odin.model.LogAccesos;
import com.odin.odin.model.SesionesUsuario;
import com.odin.odin.repository.LogAccesosRepository;
import com.odin.odin.repository.SesionesUsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class SecurityAuditService {
    private final LogAccesosRepository logRepo;
    private final SesionesUsuarioRepository sesionesRepo;
    public SecurityAuditService(LogAccesosRepository logRepo, SesionesUsuarioRepository sesionesRepo) {
        this.logRepo = logRepo; this.sesionesRepo = sesionesRepo;
    }
    public void loginExitoso(OdinUserDetails u, HttpServletRequest req) {
        LocalDateTime now = LocalDateTime.now();
        String token = req.getSession(true).getId();
        sesionesRepo.save(SesionesUsuario.builder().idUsuario(u.getIdUsuario()).fechaInicio(now).ultimaActividad(now)
                .ip(ip(req)).navegador(req.getHeader("User-Agent")).dispositivo(req.getHeader("User-Agent"))
                .estado("ACTIVA").token(token).build());
        log(u.getIdUsuario(), "LOGIN", req, true, null);
    }
    public void loginFallido(String username, HttpServletRequest req, String motivo) {
        log(null, "LOGIN_FALLIDO", req, false, motivo == null ? username : motivo);
    }
    public void logout(Long idUsuario, HttpServletRequest req) {
        String token = req.getSession(false) == null ? null : req.getSession(false).getId();
        if (token != null) sesionesRepo.findFirstByToken(token).ifPresent(s -> { s.setEstado("CERRADA"); s.setFechaFin(LocalDateTime.now()); s.setUltimaActividad(LocalDateTime.now()); sesionesRepo.save(s); });
        log(idUsuario, "LOGOUT", req, true, null);
    }
    private void log(Long id, String accion, HttpServletRequest req, boolean ok, String motivo) {
        logRepo.save(LogAccesos.builder().idUsuario(id).accion(accion).ip(ip(req)).userAgent(req.getHeader("User-Agent"))
                .exito(ok).motivo(motivo).fecha(LocalDateTime.now()).build());
    }
    private String ip(HttpServletRequest req) { String x=req.getHeader("X-Forwarded-For"); return x!=null && !x.isBlank()?x.split(",")[0].trim():req.getRemoteAddr(); }
}
