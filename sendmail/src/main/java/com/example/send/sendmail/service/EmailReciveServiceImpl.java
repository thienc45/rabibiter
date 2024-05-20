package com.example.send.sendmail.service;

import com.example.send.sendmail.dto.EmailReciveRequest;
import com.example.send.sendmail.entity.Email;
import com.example.send.sendmail.entity.EmailRecive;
import com.example.send.sendmail.repository.EmailReciveRepository;
import com.example.send.sendmail.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailReciveServiceImpl {
    @Autowired
    EmailReciveRepository emailReciveRepository;

    @Autowired
    EmailRepository emailRepository;

    public boolean addProductRecive(EmailReciveRequest emailReciveRequest) {
        EmailRecive emailRecive = new EmailRecive();
        emailRecive.setBody(emailReciveRequest.getBody());
        emailRecive.setState(0);

        Email email = emailRepository.findByName(emailReciveRequest.getNameEmail());
        if (email != null) {
            emailRecive.setEmail(email);
            emailReciveRepository.save(emailRecive);
            return true;
        } else {
            System.out.println("Email Not Found not practice email");
            return false;
        }
    }

    public EmailRecive getEmaiRecivelByName(String email) {
        Email emailFindByName = emailRepository.findByName(email);
        return emailReciveRepository.findByEmail(emailFindByName);
    }

}
