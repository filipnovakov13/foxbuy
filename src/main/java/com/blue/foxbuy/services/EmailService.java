package com.blue.foxbuy.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // For our purposes we are going to use Java Mail Sender to send e-mails
    // It automatically connects to the application.properties file and
    // derives the required web mail set up information, like smtp url,
    // username and password

    private JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Here we use the MimeMessage in combination with MimeMessageHelper
    // to compose our message.
    //
    // "MIME" stands for Multipurpose Internet Mail Extensions. It is an
    // Internet standard that extends the format of email messages to
    // support text in character sets other than ASCII, as well as attachments
    // of audio, video, images, and application programs.
    //
    // We can send messages without MimeMessageHelper
    // but it is really cumbersome, look:
    //
    // mimeMessage.setFrom(new InternetAddress("from@example.com"));
    // mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("to@example.com"));
    // mimeMessage.setSubject("Your Subject");
    // mimeMessage.setContent("<h1>Hello</h1><p>This is an HTML email.</p>", "text/html");
    //
    // mailSender.send(mimeMessage);
    //
    // Now compare it to the MimeMessageHelper:

    public void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

//  We can also send messages using SimpleMailMessage, but not HTMLs
//  or messages that contain various attachments. This would be nice
//  if we were trying to send really simple messages. Hence, the name.
//    public void sendEmail(String to, String subject, String body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//
//        message.setFrom("example@example.com");
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//
//        mailSender.send(message);
//    }
}