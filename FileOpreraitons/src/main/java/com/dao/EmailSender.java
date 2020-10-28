package com.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {
    @Autowired
	 private JavaMailSender javaMailSender;

	  @Async
	  public void sendEmail(SimpleMailMessage email) {
		try {
			  javaMailSender.send(email);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
	  }
}
