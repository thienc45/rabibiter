package com.example.send.sendmail.service;

import com.example.send.sendmail.entity.Email;
import com.example.send.sendmail.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {

    @Autowired
    EmailRepository emailRepository;


    public  void add(Email email){
        emailRepository.save(email);
    }


}
