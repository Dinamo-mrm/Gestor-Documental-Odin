package com.odin.odin.config;

import com.odin.odin.service.CustomUserDetailsService;
import com.odin.odin.service.SecurityAuditService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.http.HttpMethod;

/** Seguridad por autenticación y permisos funcionales definidos en rol_permisos. */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService,
                                                          PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationProvider authenticationProvider,
                                                   SecurityAuditService auditService) throws Exception {
        http
            .authenticationProvider(authenticationProvider)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/", "/login", "/login/**",
                        "/css/**", "/js/**", "/images/**", "/img/**", "/webjars/**", "/favicon.ico",
                        "/error", "/error/**", "/403",
                        "/recuperar-password", "/recuperar-password/**",
                        "/auth/**", "/restablecer-password", "/restablecer-password/**",
                        "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**"
                ).permitAll()
                .requestMatchers("/view/usuarios/**", "/view/roles/**", "/api/usuarios/**", "/api/roles/**").hasAnyAuthority("admin_usuarios","admin_roles","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers("/view/documentos/**", "/api/documentos/**", "/api/anexos/**", "/api/expedientes/**", "/api/firmas/**").hasAnyAuthority("gestionar_documentos","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers("/view/tramites/**", "/api/tramites/**").hasAnyAuthority("ver_tramites","gestionar_tramites","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers("/view/consulta").hasAnyAuthority("ver_radicados","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers("/view/bitacora/**", "/view/plantilla/**").hasAnyAuthority("ver_bitacora","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers("/view/reasignaciones/**", "/api/reasignaciones/**").hasAnyAuthority("trasladar_radicado","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers("/view/reportes/**").hasAnyAuthority("ver_reportes","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers("/view/dependencias/**", "/view/series/**", "/view/subseries/**", "/view/estados/**", "/api/dependencias/**", "/api/series/**", "/api/subseries/**", "/api/estados/**").hasAnyAuthority("ver_dependencias","admin_ccd","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers(HttpMethod.POST, "/view/radicados/delete/**").hasAnyAuthority("finalizar_radicado","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers(HttpMethod.POST, "/view/radicados/**").hasAnyAuthority("crear_radicado","editar_radicado","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers(HttpMethod.POST, "/api/radicados/**").hasAnyAuthority("crear_radicado","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers(HttpMethod.PUT, "/api/radicados/**").hasAnyAuthority("editar_radicado","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers(HttpMethod.DELETE, "/api/radicados/**").hasAnyAuthority("finalizar_radicado","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers("/api/radicados/**", "/view/radicados/**").hasAnyAuthority("ver_radicados","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers("/api/reportes/**").hasAnyAuthority("ver_reportes","ROLE_ADMIN","ROLE_ROOT")
                .requestMatchers("/api/notificaciones/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/view/dashboard", true)
                .failureUrl("/login?error=true")
                .failureHandler(new SecurityAuditFailureHandler(auditService))
                .successHandler(new SecurityAuditSuccessHandler(auditService))
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .addLogoutHandler((request, response, authentication) -> {
                    if (authentication != null && authentication.getPrincipal() instanceof com.odin.odin.service.OdinUserDetails u) auditService.logout(u.getIdUsuario(), request);
                })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex.accessDeniedPage("/403"))
            .sessionManagement(session -> session
                .sessionFixation(sessionFixation -> sessionFixation.migrateSession())
                .maximumSessions(1));
        return http.build();
    }
}
