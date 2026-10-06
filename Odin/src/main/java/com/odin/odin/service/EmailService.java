package com.odin.odin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username:}") private String correoRemitente;
    public EmailService(JavaMailSender mailSender) { this.mailSender = mailSender; }
    public void enviarCodigoRecuperacion(String destinatario, String nombre, String codigo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        if (correoRemitente != null && !correoRemitente.isBlank()) mensaje.setFrom(correoRemitente);
        mensaje.setTo(destinatario);
        mensaje.setSubject("Código de recuperación - ODIN");
        mensaje.setText("Hola " + (nombre == null ? "" : nombre) + ",\n\n" +
                "Recibimos una solicitud para restablecer la contraseña de tu cuenta en ODIN.\n\n" +
                "Tu código de recuperación es: " + codigo + "\n\n" +
                "Este código vence en 10 minutos y solo puede usarse una vez.\n\n" +
                "Si no solicitaste este cambio, ignora este mensaje.\n\nODIN - Sistema de Gestión Documental");
        mailSender.send(mensaje);
    }
}
