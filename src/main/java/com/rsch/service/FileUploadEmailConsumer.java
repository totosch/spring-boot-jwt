package com.rsch.service;

import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class FileUploadEmailConsumer {

    private final EmailService emailService;

    public FileUploadEmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RetryableTopic(
            attempts = "3",
            backOff = @BackOff(delay = 2000)
    )
    @KafkaListener(topics = "upload-file-topic", groupId = "file-upload-group")
    public void listenFileUpload(String email) {
        System.out.println("kafka received a pending file upload email to: " + email);
        emailService.sendEmailWhenUserUploadsFile(email);
    }

    @DltHandler
    public void processDLQMessage(String email,
                                  @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage) {
        System.err.println("[DLQ] email to " + email + " failed");
        System.err.println("error message: " + errorMessage);
    }
}