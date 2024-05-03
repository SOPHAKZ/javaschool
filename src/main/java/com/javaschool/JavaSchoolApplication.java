package com.javaschool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
public class JavaSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaSchoolApplication.class, args);
    }

    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
}
