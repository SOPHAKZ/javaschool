package com.javaschool.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, int otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("javaschool@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject("Your New Account Password");
        helper.setText("Dear User,\n\n your opt code is: " + otp + "\n\nPlease keep this password confidential and do not share it with anyone.\n\nBest regards,\nThe System Team");

        mailSender.send(message);
    }
}
