package com.odin.odin.config;

import com.odin.odin.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {


    private final CustomUserDetailsService userDetailsService;


    public SecurityConfig(
            CustomUserDetailsService userDetailsService) {

        this.userDetailsService =
                userDetailsService;
    }



    /*
     * =========================================================
     * PASSWORD ENCODER
     * =========================================================
     */

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }



    /*
     * =========================================================
     * AUTHENTICATION PROVIDER
     * =========================================================
     */

    @Bean
    public AuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder) {


        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        userDetailsService
                );


        provider.setPasswordEncoder(
                passwordEncoder
        );


        return provider;
    }



    /*
     * =========================================================
     * SPRING SECURITY
     * =========================================================
     */

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider)
            throws Exception {


        http


                /*
                 * =================================================
                 * PROVEEDOR DE AUTENTICACIÓN
                 * =================================================
                 */

                .authenticationProvider(
                        authenticationProvider
                )



                /*
                 * =================================================
                 * AUTORIZACIÓN
                 * =================================================
                 */

                .authorizeHttpRequests(

                        auth -> auth


                                /*
                                 * ---------------------------------
                                 * RUTAS PÚBLICAS
                                 * ---------------------------------
                                 *
                                 * IMPORTANTE:
                                 *
                                 * /logout-exitoso tiene que ser
                                 * público porque cuando llegamos
                                 * allí el usuario YA cerró sesión.
                                 */

                                .requestMatchers(
                                        "/login",
                                        "/logout-exitoso",
                                        "/recuperar-password",
                                        "/recuperar-password/**",
                                        "/css/**",
                                        "/js/**",
                                        "/images/**",
                                        "/img/**",
                                        "/favicon.ico",
                                        "/error"
                                )
                                .permitAll()



                                /*
                                 * ---------------------------------
                                 * ERROR 403
                                 * ---------------------------------
                                 */

                                .requestMatchers(
                                        "/403"
                                )
                                .authenticated()



                                /*
                                 * ---------------------------------
                                 * ADMINISTRACIÓN DE USUARIOS
                                 * ---------------------------------
                                 */

                                .requestMatchers(
                                        "/view/usuarios/**",
                                        "/api/usuarios/**"
                                )
                                .hasAuthority(
                                        "admin_usuarios"
                                )



                                /*
                                 * ---------------------------------
                                 * ROLES
                                 * ---------------------------------
                                 */

                                .requestMatchers(
                                        "/view/roles/**",
                                        "/api/roles/**"
                                )
                                .hasAuthority(
                                        "admin_roles"
                                )



                                /*
                                 * ---------------------------------
                                 * REPORTES
                                 * ---------------------------------
                                 */

                                .requestMatchers(
                                        "/view/reportes/**",
                                        "/api/reportes/**"
                                )
                                .hasAuthority(
                                        "ver_reportes"
                                )



                                /*
                                 * ---------------------------------
                                 * BITÁCORA
                                 * ---------------------------------
                                 */

                                .requestMatchers(
                                        "/view/bitacora/**",
                                        "/api/bitacora/**"
                                )
                                .hasAuthority(
                                        "ver_bitacora"
                                )



                                /*
                                 * ---------------------------------
                                 * RESTO DEL SISTEMA
                                 * ---------------------------------
                                 */

                                .anyRequest()
                                .authenticated()
                )



                /*
                 * =================================================
                 * LOGIN
                 * =================================================
                 */

                .formLogin(

                        form -> form


                                /*
                                 * Vista de login
                                 */

                                .loginPage(
                                        "/login"
                                )


                                /*
                                 * Spring procesa aquí
                                 * el formulario.
                                 */

                                .loginProcessingUrl(
                                        "/login"
                                )


                                /*
                                 * Coincide EXACTAMENTE con:
                                 *
                                 * <input name="correo">
                                 *
                                 * de auth/login.html.
                                 */

                                .usernameParameter(
                                        "correo"
                                )


                                /*
                                 * Coincide con:
                                 *
                                 * <input name="password">
                                 */

                                .passwordParameter(
                                        "password"
                                )


                                /*
                                 * Login exitoso
                                 */

                                .defaultSuccessUrl(
                                        "/view/dashboard",
                                        true
                                )


                                /*
                                 * Login incorrecto
                                 */

                                .failureUrl(
                                        "/login?error=true"
                                )


                                .permitAll()
                )



                /*
                 * =================================================
                 * LOGOUT
                 * =================================================
                 */

                .logout(

                        logout -> logout


                                /*
                                 * El layout envía:
                                 *
                                 * POST /logout
                                 */

                                .logoutUrl(
                                        "/logout"
                                )


                                /*
                                 * CAMBIO IMPORTANTE:
                                 *
                                 * Antes:
                                 *
                                 * /login?logout=true
                                 *
                                 * Ahora:
                                 *
                                 * /logout-exitoso
                                 */

                                .logoutSuccessUrl(
                                        "/logout-exitoso"
                                )


                                /*
                                 * Destruye la sesión HTTP.
                                 */

                                .invalidateHttpSession(
                                        true
                                )


                                /*
                                 * Limpia la autenticación.
                                 */

                                .clearAuthentication(
                                        true
                                )


                                /*
                                 * Elimina la cookie de sesión.
                                 */

                                .deleteCookies(
                                        "JSESSIONID"
                                )


                                .permitAll()
                )



                /*
                 * =================================================
                 * ACCESO DENEGADO
                 * =================================================
                 */

                .exceptionHandling(

                        exception -> exception

                                .accessDeniedHandler(
                                        (request, response, accessDeniedException) ->
                                                response.sendRedirect(
                                                        request.getContextPath() + "/403"
                                                )
                                )
                )



                /*
                 * =================================================
                 * SESIÓN
                 * =================================================
                 */

                .sessionManagement(

                        session -> session

                                .sessionFixation()
                                .migrateSession()

                                .maximumSessions(1)
                );


        return http.build();
    }
}