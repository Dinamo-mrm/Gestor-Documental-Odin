package com.odin.odin.controller;

import com.odin.odin.service.PasswordResetService;
import com.odin.odin.service.PasswordResetService.RecuperacionResult;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/recuperar-password")
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping
    public String formulario(Authentication authentication, Model model) {
        if (autenticado(authentication)) return "redirect:/view/dashboard";
        model.addAttribute("recuperacionHabilitada", false);
        return "auth/recuperar-password";
    }

    @PostMapping
    public String solicitar(@RequestParam(value = "username", required = false) String username,
                            @RequestParam(value = "correo", required = false) String correo,
                            Model model) {
        String login = (username != null && !username.isBlank()) ? username : correo;
        model.addAttribute("username", login);
        model.addAttribute("recuperacionHabilitada", false);

        Optional<RecuperacionResult> resultado = Optional.empty();
        try {
            resultado = passwordResetService.solicitarRecuperacion(login);
        } catch (Exception ignored) {
        }

        if (resultado.isEmpty()) {
            model.addAttribute("errorValidacion",
                    "No se encontró una cuenta activa con esos datos. Verifica correo, identificación o nombre.");
            return "auth/recuperar-password";
        }

        RecuperacionResult r = resultado.get();
        model.addAttribute("recuperacionHabilitada", true);
        model.addAttribute("token", r.getToken());
        model.addAttribute("correoEnmascarado", r.getCorreoEnmascarado());
        model.addAttribute("nombreUsuario", r.getNombre());
        model.addAttribute("mensajeOk",
                "Datos validados. Se envió un enlace a " + r.getCorreoEnmascarado()
                        + ". También puedes definir tu nueva contraseña aquí.");
        return "auth/recuperar-password";
    }

    @GetMapping("/restablecer")
    public String restablecerFormulario(@RequestParam String token, Model model) {
        if (!passwordResetService.tokenValido(token)) {
            model.addAttribute("tokenInvalido", true);
            return "auth/restablecer-password";
        }
        model.addAttribute("token", token);
        return "auth/restablecer-password";
    }

    @PostMapping("/restablecer")
    public String restablecer(@RequestParam String token,
                              @RequestParam String password,
                              @RequestParam String confirmPassword,
                              Model model) {
        if (password == null || password.length() < 8) {
            model.addAttribute("error", "La contraseña debe tener al menos 8 caracteres.");
            model.addAttribute("token", token);
            model.addAttribute("recuperacionHabilitada", true);
            return "auth/recuperar-password";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            model.addAttribute("token", token);
            model.addAttribute("recuperacionHabilitada", true);
            return "auth/recuperar-password";
        }
        if (!passwordResetService.restablecer(token, password)) {
            model.addAttribute("tokenInvalido", true);
            return "auth/restablecer-password";
        }
        return "redirect:/login?reset=true";
    }

    private boolean autenticado(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
