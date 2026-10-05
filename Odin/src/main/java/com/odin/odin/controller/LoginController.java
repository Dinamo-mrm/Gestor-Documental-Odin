package com.odin.odin.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/")
    public String inicio(Authentication authentication) {
        return estaAutenticado(authentication) ? "redirect:/view/dashboard" : "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        return estaAutenticado(authentication) ? "redirect:/view/dashboard" : "auth/login";
    }

    @GetMapping({"/auth/recuperar-password", "/auth/login", "/auth/restablecer-password"})
    public String aliasAuth(HttpServletRequest req) {
        String uri = req.getRequestURI() == null ? "" : req.getRequestURI();
        if (uri.contains("recuperar") || uri.contains("restablecer")) {
            return "redirect:/recuperar-password";
        }
        return "redirect:/login";
    }

    @GetMapping("/403")
    public String accesoDenegado() {
        return "error/403";
    }

    private boolean estaAutenticado(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
