package com.odin.odin.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {


    @GetMapping("/")
    public String inicio(
            Authentication authentication) {


        if (estaAutenticado(authentication)) {

            return "redirect:/view/dashboard";
        }


        return "redirect:/login";
    }



    @GetMapping("/login")
    public String login(
            Authentication authentication) {


        if (estaAutenticado(authentication)) {

            return "redirect:/view/dashboard";
        }


        return "auth/login";
    }



    @GetMapping("/403")
    public String accesoDenegado() {

        return "error/403";
    }



    private boolean estaAutenticado(
            Authentication authentication) {


        return authentication != null

                && authentication.isAuthenticated()

                && !(authentication
                instanceof AnonymousAuthenticationToken);
    }
}