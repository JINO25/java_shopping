package com.example.project_shopping.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private Integer port;
    @Value("${spring.mail.username}")
    private String userName;
    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        javaMailSender.setHost(host);
//        javaMailSender.setPort(port);
//        javaMailSender.setUsername(userName);
//        javaMailSender.setPassword(password);
        javaMailSender.setHost("sandbox.smtp.mailtrap.io");
        javaMailSender.setPort(2525);
        javaMailSender.setUsername("34289ddfe307f7");
        javaMailSender.setPassword("6344d2c1aca2ad");

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.debug", "true");

        return javaMailSender;
    }
}
