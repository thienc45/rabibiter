package com.example.send.sendmail.controller;


import com.example.send.sendmail.constant.KeyRabiter;
import com.example.send.sendmail.dto.EmailReciveListRequest;
import com.example.send.sendmail.dto.EmailReciveRequest;
import com.example.send.sendmail.entity.Email;
import com.example.send.sendmail.entity.EmailRecive;
import com.example.send.sendmail.service.EmailReciveServiceImpl;
import com.example.send.sendmail.service.SendmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    EmailReciveServiceImpl emailReciveService;


    @Autowired
    private SendmailService resendService;

    public UserController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody EmailReciveListRequest emailListRequest) throws JsonProcessingException {
        Map<String, String> response = new HashMap<>();
        boolean allSuccess = true;

        for (String email : emailListRequest.getNameEmails()) {
            // TODO save emailReciveRequest in the database
            EmailReciveRequest emailReciveRequest = new EmailReciveRequest();
            emailReciveRequest.setBody(emailListRequest.getBody());
            emailReciveRequest.setNameEmail(email);

            if (!emailReciveService.addProductRecive(emailReciveRequest)) {
                allSuccess = false;
                continue;
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                String queuePayloadString = objectMapper.writeValueAsString(emailListRequest);
                rabbitTemplate.convertAndSend(KeyRabiter.QUEUE_NAME, queuePayloadString);
            }
        }

        if (allSuccess) {
            response.put("message", "All users registered successfully!");
        } else {
            response.put("message", "User registration failed for one or more users!");
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkSendEmail")
    public ResponseEntity<Map<String, String>> checkSendEmail(@RequestParam String email) {
        EmailRecive emailEntity = emailReciveService.getEmaiRecivelByName(email);
        Map<String, String> response = new HashMap<>();

        if (emailEntity == null) {
            response.put("message", "Email not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (emailEntity.getState() == 1) {
            response.put("message", "Email has been revcive");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (emailEntity.getState() == 0) {
            response.put("message", "Email has not been revcive");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "Unknown state");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
