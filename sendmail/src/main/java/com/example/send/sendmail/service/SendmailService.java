package com.example.send.sendmail.service;

import com.resend.Resend;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String email,String body) {
        String from = "daspabitra55@gmail.com";
        String to = email;
        String subject = "Account Verification";
        String content = "[[name]],<br>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // Use true for multipart message

            helper.setFrom(from, "Becoder");
            helper.setTo(to);
            helper.setSubject(subject);

            content = content.replace("[[name]]", body);
            System.out.println(content);
            helper.setText(content, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
