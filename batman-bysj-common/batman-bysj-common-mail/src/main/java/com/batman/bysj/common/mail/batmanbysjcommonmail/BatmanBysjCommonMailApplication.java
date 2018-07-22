package com.batman.bysj.common.mail.batmanbysjcommonmail;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class BatmanBysjCommonMailApplication implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {

//		MailClient mailClient = MailClientFactory.fromDefaultProperties();
//
//		Mail mail = new Mail()
//				.setSubject("Hello, World!")
//				.setContentType("text/html")
//				.setContent("<h1>This is a test mail!</h1>")
//				.setReceivers(Collections.singletonList("787793569@qq.com"));
//
//		mailClient.send(mail);
	}

	public static void main(String[] args) {
		SpringApplication.run(BatmanBysjCommonMailApplication.class, args);
	}
}
