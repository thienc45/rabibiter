package com.example.receive.receiverabiitmq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmaiReciveDto {
    Long id;

    String body;

    Integer state;

    String nameEmail;
}
