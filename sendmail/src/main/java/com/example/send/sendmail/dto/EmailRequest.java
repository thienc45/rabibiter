package com.example.send.sendmail.dto;

public record EmailRequest(String to, String subject, String body) {
}
