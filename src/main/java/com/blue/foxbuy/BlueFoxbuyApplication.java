package com.blue.foxbuy;

import com.blue.foxbuy.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.Arrays;

@SpringBootApplication
public class BlueFoxbuyApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlueFoxbuyApplication.class, args);
    }
}