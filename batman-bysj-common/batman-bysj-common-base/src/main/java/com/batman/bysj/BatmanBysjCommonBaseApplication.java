package com.batman.bysj;

import com.batman.bysj.common.mail.batmanbysjcommonmail.Mail;
import com.batman.bysj.common.mail.batmanbysjcommonmail.MailClient;
import com.batman.bysj.common.mail.batmanbysjcommonmail.MailClientFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class BatmanBysjCommonBaseApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(BatmanBysjCommonBaseApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        MailClient mailClient = MailClientFactory.fromDefaultProperties();

        Mail mail = new Mail()
                .setSubject("Hello, World!")
                .setContentType("text/html")
                .setContent("<h1>This is a test mail!</h1>")
                .setReceivers(Collections.singletonList("943104990@qq.com"));

        mailClient.send(mail);
    }

}
