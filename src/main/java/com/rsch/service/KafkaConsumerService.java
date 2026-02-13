package com.rsch.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final EmailService emailService;

    public KafkaConsumerService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-registration-topic", groupId = "welcome-email-group")
    public void listen(String email) {
        System.out.println("kafka received a pending email to: " + email);
        emailService.sendWelcomeEmail(email);
    }

    @KafkaListener(topics = "upload-file-topic", groupId = "file-upload-group")
    public void listenFileUpload(String email){
        System.out.println("kafka received a pending email to:" + email);
        emailService.sendEmailWhenUserUploadsFile(email);
    }
}