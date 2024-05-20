package com.example.receive.receiverabiitmq.test;

import com.example.receive.receiverabiitmq.dto.EmailReciveListDto;
import com.fasterxml.jackson.databind.ObjectMapper;

public class sdfg { public static void main(String[] args) {
    String jsonMessage = "{\"body\": \"thien Doe\", \"nameEmails\": [\"thienczai123@gmail.com\", \"tdoanduc853@gmail.com\"]}";

    ObjectMapper objectMapper = new ObjectMapper();
    try {
        EmailReciveListDto messageData = objectMapper.readValue(jsonMessage, EmailReciveListDto.class);
        System.out.println("Deserialized message data: " + messageData);
        System.out.println("Body: " + messageData.getBody());
        System.out.println("Name Emails: " + messageData.getNameEmails());
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
