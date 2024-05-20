package com.example.receive.receiverabiitmq.listeners;

import com.example.receive.receiverabiitmq.dto.EmailReciveListDto;
import com.example.receive.receiverabiitmq.entity.Email;
import com.example.receive.receiverabiitmq.entity.EmailRecive;
import com.example.receive.receiverabiitmq.repository.EmailReciveRepository;
import com.example.receive.receiverabiitmq.repository.EmailRepository;
import com.example.receive.receiverabiitmq.service.SendmailService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRegisteredListener {

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    EmailReciveRepository emailReciveRepository;

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    SendmailService sendmailService;

    public void onMessageReceived(String jsonMessage) {
        try {
            System.out.println(jsonMessage);
            EmailReciveListDto messageData = objectMapper.readValue(jsonMessage, EmailReciveListDto.class);

            System.out.println(messageData.toString());

            String body = messageData.getBody();
            List<String> nameEmails = messageData.getNameEmails();

            for (String nameEmail : nameEmails) {
                System.out.println("Received body: " + body);
                System.out.println("Received nameEmail: " + nameEmail);

                Email email = emailRepository.findByName(nameEmail);
                EmailRecive emailRecive = emailReciveRepository.findByEmailAndState(email, 0);
                if (emailRecive != null) {
                    emailRecive.setState(1);
                    emailReciveRepository.save(emailRecive);
                }
                sendmailService.sendEmail(nameEmail, body);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
