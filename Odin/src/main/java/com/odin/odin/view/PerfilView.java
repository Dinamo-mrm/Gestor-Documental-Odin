package com.odin.odin.view;

import com.odin.odin.model.Usuarios;
import com.odin.odin.repository.UsuariosRepository;
import com.odin.odin.service.OdinUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/view/perfil")
public class PerfilView {

    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;

    public PerfilView(UsuariosRepository usuariosRepository, PasswordEncoder passwordEncoder) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private OdinUserDetails getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof OdinUserDetails oud) {
            return oud;
        }
        return null;
    }

    @GetMapping("/cambiar-password")
    public String formCambiarPassword(Model model) {
        OdinUserDetails u = getUsuarioAutenticado();
        if (u == null) {
            return "redirect:/login";
        }
        return "perfil/cambiarPassword";
    }

    @PostMapping("/cambiar-password")
    public String procesarCambiarPassword(@RequestParam(value = "passwordActual", required = false) String passwordActual,
                                          @RequestParam(value = "nuevaPassword", required = false) String nuevaPassword,
                                          @RequestParam(value = "confirmarPassword", required = false) String confirmarPassword,
                                          Model model,
                                          RedirectAttributes ra) {
        OdinUserDetails u = getUsuarioAutenticado();
        if (u == null) {
            return "redirect:/login";
        }

        Usuarios usuario = usuariosRepository.findById(u.getIdUsuario()).orElse(null);
        if (usuario == null) {
            ra.addFlashAttribute("error", "No se encontró el usuario en el sistema.");
            return "redirect:/view/dashboard";
        }

        if (passwordActual == null || passwordActual.isBlank()) {
            model.addAttribute("error", "Debes ingresar tu contraseña actual.");
            return "perfil/cambiarPassword";
        }

        if (!passwordEncoder.matches(passwordActual.trim(), usuario.getPassword())) {
            model.addAttribute("error", "La contraseña actual ingresada es incorrecta.");
            return "perfil/cambiarPassword";
        }

        if (nuevaPassword == null || nuevaPassword.trim().length() < 6) {
            model.addAttribute("error", "La nueva contraseña debe tener al menos 6 caracteres.");
            return "perfil/cambiarPassword";
        }

        if (!nuevaPassword.equals(confirmarPassword)) {
            model.addAttribute("error", "La nueva contraseña y su confirmación no coinciden.");
            return "perfil/cambiarPassword";
        }

        if (usuario.getNum_identificacion() != null && nuevaPassword.trim().equals(usuario.getNum_identificacion().trim())) {
            model.addAttribute("error", "Por seguridad, la nueva contraseña no puede ser igual a tu número de identificación.");
            return "perfil/cambiarPassword";
        }

        // Guardar la nueva contraseña cifrada
        usuario.setPassword(passwordEncoder.encode(nuevaPassword.trim()));
        usuariosRepository.save(usuario);

        // Desactivar la alerta de cambio de contraseña en la sesión activa
        u.setDebeCambiarPassword(false);

        ra.addFlashAttribute("success", "¡Contraseña actualizada con éxito! Tu cuenta ahora está protegida.");
        return "redirect:/view/dashboard";
    }
}
