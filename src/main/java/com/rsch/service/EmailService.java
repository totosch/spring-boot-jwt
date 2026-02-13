package com.rsch.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(String toEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@rsch.com");
            message.setTo(toEmail);
            message.setSubject("¡Welcome to rsch!");
            message.setText("Hi!\n\nThank you for joining rsch.\nYou can now upload your files.\n\nGreetings,\nrsch.");

            mailSender.send(message);
            System.out.println("new user email sent successfully " + toEmail);
        } catch (Exception e) {
            System.err.println("error sending email " + toEmail + ": " + e.getMessage());
        }
    }

    public void sendEmailWhenUserUploadsFile(String toEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@rsch.com");
            message.setTo(toEmail);
            message.setSubject("File upload successfully!");
            message.setText("Your file has been upload successfully!");

            mailSender.send(message);
            System.out.println("upload file email sent successfully " + toEmail);
        } catch (Exception e) {
            System.err.println("error sending email " + toEmail + ": " + e.getMessage());
        }
    }
}