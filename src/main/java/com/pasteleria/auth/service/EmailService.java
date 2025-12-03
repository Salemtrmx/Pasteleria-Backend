package com.pasteleria.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.from:noreply@pasteleria.com}")
    private String fromEmail;

    @Value("${app.mail.from.name:Pastelería}")
    private String fromName;

    public void enviarCodigoRecuperacion(String destinatario, String codigo, String nombreUsuario) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setFrom(fromEmail, fromName);
        helper.setTo(destinatario);
        helper.setSubject("Recuperacion de contrasena - Pasteleria");
        
        String htmlContent = "<!DOCTYPE html>" +
            "<html>" +
            "<head><meta charset=\"UTF-8\"></head>" +
            "<body style=\"font-family: 'Segoe UI', Arial, sans-serif; background-color: #fff9f0; margin: 0; padding: 20px;\">" +
            "<div style=\"max-width: 500px; margin: 0 auto; background: white; border-radius: 20px; overflow: hidden; box-shadow: 0 10px 40px rgba(247, 176, 91, 0.2);\">" +
            
            "<!-- Header -->" +
            "<div style=\"background: linear-gradient(135deg, #F7B05B, #f5a142); padding: 30px; text-align: center;\">" +
            "<h1 style=\"color: white; margin: 0; font-size: 28px;\">Pasteleria Baksy</h1>" +
            "<p style=\"color: rgba(255,255,255,0.9); margin: 10px 0 0 0; font-size: 14px;\">Recuperacion de contrasena</p>" +
            "</div>" +
            
            "<!-- Content -->" +
            "<div style=\"padding: 40px 30px;\">" +
            "<h2 style=\"color: #333; margin: 0 0 15px 0; font-size: 22px;\">Hola, " + nombreUsuario + "!</h2>" +
            "<p style=\"color: #666; font-size: 15px; line-height: 1.6; margin: 0 0 25px 0;\">" +
            "Recibimos una solicitud para restablecer tu contrasena. Usa el siguiente codigo de verificacion:" +
            "</p>" +
            
            "<!-- Codigo -->" +
            "<div style=\"background: linear-gradient(135deg, #fff9f0, #ffe4c4); border-radius: 15px; padding: 25px; text-align: center; margin: 25px 0;\">" +
            "<p style=\"color: #999; font-size: 12px; margin: 0 0 10px 0; text-transform: uppercase; letter-spacing: 1px;\">Tu codigo de verificacion</p>" +
            "<h1 style=\"color: #F7B05B; font-size: 42px; letter-spacing: 8px; margin: 0; font-weight: bold;\">" + codigo + "</h1>" +
            "</div>" +
            
            "<p style=\"color: #999; font-size: 13px; line-height: 1.6; margin: 25px 0 0 0;\">" +
            "Este codigo expira en <strong>15 minutos</strong>.<br>" +
            "Si no solicitaste este cambio, ignora este correo." +
            "</p>" +
            "</div>" +
            
            "<!-- Footer -->" +
            "<div style=\"background: #f8f9fa; padding: 20px 30px; text-align: center; border-top: 1px solid #eee;\">" +
            "<p style=\"color: #999; font-size: 12px; margin: 0;\">2025 Pasteleria Baksy. Todos los derechos reservados.</p>" +
            "</div>" +
            "</div>" +
            "</body>" +
            "</html>";

        helper.setText(htmlContent, true);
        
        mailSender.send(mensaje);
        System.out.println("Correo de recuperacion enviado a: " + destinatario);
    }
}
