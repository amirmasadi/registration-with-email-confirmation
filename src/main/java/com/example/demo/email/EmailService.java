package com.example.demo.email;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService implements EmailSender {
  @Autowired
  private JavaMailSender javaMailSender;


  @Override
  @Async
  public void send(String to, String email) {
    try {
      MimeMessage message = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
      helper.setTo(to);
      helper.setText(email, true);
      helper.setSubject("Confirm your email");
      helper.setFrom("amirmasadi@outlook.com");
      javaMailSender.send(message);
    } catch (MessagingException e) {
      log.error("Failed to send email", e);
      throw new IllegalStateException("Failed to send email");
    }
  }
}
