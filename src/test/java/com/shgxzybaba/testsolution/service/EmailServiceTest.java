package com.shgxzybaba.testsolution.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EmailServiceTest {

    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    EmailService emailService;



    @BeforeEach
    void setUp() {
        doNothing().when(mailSender).send(any(MimeMessage.class));
        when(mailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));

    }

    @Test
    void sendEmail() {
        String subject = "Test subject!";
        String content = "Test content";
        String receiver = "example@xyz.com";

        emailService.sendEmail(content, subject, receiver);
        verify(mailSender).send(any(MimeMessage.class));

    }
}