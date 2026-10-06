package com.odin.odin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LogoutController {


    /*
     * =========================================================
     * PANTALLA POSTERIOR AL LOGOUT
     * =========================================================
     *
     * IMPORTANTE:
     *
     * Este controlador NO cierra la sesión.
     *
     * Spring Security ya hizo:
     *
     * POST /logout
     *      ↓
     * elimina autenticación
     *      ↓
     * invalida sesión
     *      ↓
     * elimina JSESSIONID
     *      ↓
     * redirige aquí
     *
     * /logout-exitoso
     */

    @GetMapping("/logout-exitoso")
    public String logoutExitoso() {

        return "auth/logout";
    }
}