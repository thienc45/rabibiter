package com.example.send.sendmail.dto;

public record UserRegisteredPayload(String fullName, String emailAddress, int confirmationCode) {
}
