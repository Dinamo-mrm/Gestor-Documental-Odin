package com.odin.odin.config;

import com.odin.odin.model.Radicados;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.NotificacionesRepository;
import com.odin.odin.model.Notificaciones;
import com.odin.odin.service.OdinUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class UsuarioModelAdvice {

    private final RadicadosRepository radicadosRepository;
    private final NotificacionesRepository notificacionesRepository;

    public UsuarioModelAdvice(RadicadosRepository radicadosRepository, NotificacionesRepository notificacionesRepository) {
        this.radicadosRepository = radicadosRepository;
        this.notificacionesRepository = notificacionesRepository;
    }

    @ModelAttribute("usuarioActual")
    public OdinUserDetails usuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        Object p = auth.getPrincipal();
        return (p instanceof OdinUserDetails oud) ? oud : null;
    }

    @ModelAttribute("nombreUsuario")
    public String nombreUsuario() {
        OdinUserDetails u = usuarioActual();
        if (u != null) return u.getNombreVisible();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getName() != null
                && !"anonymousUser".equals(auth.getName())) {
            return auth.getName();
        }
        return "Usuario";
    }

    @ModelAttribute("inicialUsuario")
    public String inicialUsuario() {
        OdinUserDetails u = usuarioActual();
        if (u != null) return u.getInicial();
        String n = nombreUsuario();
        return (n == null || n.isBlank()) ? "U" : n.substring(0, 1).toUpperCase();
    }

    @ModelAttribute("correoUsuario")
    public String correoUsuario() {
        OdinUserDetails u = usuarioActual();
        return u != null && u.getCorreo() != null ? u.getCorreo() : "";
    }

    @ModelAttribute("notifCount")
    public long notifCount() {
        OdinUserDetails u = usuarioActual();
        if (u == null || u.getIdUsuario() == null) return 0L;
        try { return notificacionesRepository.countById_usuarioAndLeidaFalse(u.getIdUsuario()); }
        catch (Exception e) { return 0L; }
    }

    @ModelAttribute("notifItems")
    public List<String> notifItems() {
        OdinUserDetails u = usuarioActual();
        if (u == null || u.getIdUsuario() == null) return Collections.singletonList("No hay notificaciones nuevas");
        try {
            List<Notificaciones> ns = notificacionesRepository.findTop10ById_usuarioOrderByFechaDesc(u.getIdUsuario());
            if (ns == null || ns.isEmpty()) return Collections.singletonList("No hay notificaciones nuevas");
            List<String> items = new ArrayList<>();
            for (Notificaciones n : ns) items.add((n.getTitulo() == null ? "Aviso" : n.getTitulo()) + " — " + (n.getMensaje() == null ? "" : n.getMensaje()));
            return items;
        } catch (Exception e) { return Collections.singletonList("No hay notificaciones nuevas"); }
    }
}
