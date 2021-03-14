package ru.somarov.templates.java.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;
    @Value("${mail.receiver}")
    private String receiver;

    public void send(String emailTo, String subject, String message){

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(receiver);
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setText(message);
        simpleMailMessage.setSubject(subject);
        javaMailSender.send(simpleMailMessage);
    }


    public void sendToSender(String emailTo, String subject, String message){

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailTo);
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setText("Копия сообщения в компанию Фабрика Дерева:/n" + message);
        simpleMailMessage.setSubject(subject);
        javaMailSender.send(simpleMailMessage);
    }


}
